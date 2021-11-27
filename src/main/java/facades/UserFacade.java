package facades;

import entities.LoginAttempts;
import entities.User;
import entities.Role;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import entities.UserDTO;
import security.errorhandling.AuthenticationException;
import utils.MailSystem;

import java.util.*;

public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;


    private UserFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password, String ip) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        MaliciousIntentFacade mIF = MaliciousIntentFacade.getMaliciousIntentFacade(emf);
        User endUser = findUserByUsername(username);
        LoginAttempts lA = new LoginAttempts(ip, username, "login");

        if (userPassedValidation(endUser)){
            throw new AuthenticationException("You did not verify your email, your account has been deleted.");
        }
        if (mIF.isBanned(lA)) {
            throw new AuthenticationException("Your Ip is banned, try again in 10 hours");
        }
        if (mIF.getLoggedAttempts(lA) >= 3) {
            mIF.createBan(lA);
            throw new AuthenticationException("Too many login attempts, try again in 10 hours");
        }
        try {
            if (endUser != null && !endUser.verifyPassword(password)) {
                mIF.logAttempt(lA);
                throw new AuthenticationException("Invalid username or password");
             }
            if (endUser == null || !endUser.verifyPassword(password)) {
                mIF.logAttempt(lA);
                throw new AuthenticationException("Invalid username or password");
            }
        } finally {
            em.close();
        }
        return endUser;
    }

    public User createUser(User endUser) {
        EntityManager em = emf.createEntityManager();
        User userforPersist = new User(endUser.getUsername(), endUser.getPassword(), "");
        userforPersist.setVerified(false);
        String uuid = UUID.randomUUID().toString();
        String longerUuid = uuid + UUID.randomUUID();

        em.getTransaction().begin();
        Role userRole = new Role("user");
        userforPersist.addRole(userRole);
        userforPersist.setVerificationKey(longerUuid);
        long theFuture = System.currentTimeMillis() + (86400 * 7 * 1000);
        Date oneWeekFromNow = new Date(theFuture);
        userforPersist.setTtlBeforeDeletion(oneWeekFromNow);
        em.persist(userforPersist);
        em.getTransaction().commit();


        MailSystem ms = new MailSystem();
        ms.verifyUsersEmail(endUser.getUsername(), longerUuid);
        return userforPersist;
    }

    public User findUserByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            em.getTransaction().begin();
            TypedQuery<User> foundUser = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            foundUser.setParameter("username", username);
            em.getTransaction().commit();
            try {
                user = foundUser.getSingleResult();
            } catch(NoResultException e) {
                return null;
            }

        } finally {
            em.close();
        }
        return user;
    }

    public boolean updatePasswordForUser(String email, String newPass, String key) {
        EntityManager em = emf.createEntityManager();
        User foundUser = findUserByUsername(email);
        System.out.println(foundUser);
        if (foundUser.getResetPassKey().equals(key)) {
            System.out.println("passer");
            foundUser.setPassword(newPass);
            em.getTransaction().begin();
            em.merge(foundUser);
            em.getTransaction().commit();
            em.close();
            return true;
        }
        System.out.println("passer ikke");
        return false;
    }

    public void updateUserProfile(UserDTO user) {
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        TypedQuery<User> foundUser = em.createQuery("UPDATE User u SET u.profileText = :profileText WHERE u.username = :email", User.class);
        foundUser.setParameter("profileText", user.getProfileText());
        foundUser.setParameter("email", user.getEmail());
        foundUser.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    public void setUserRecoveryKey(String username, String ip) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, username);
        String uuid = UUID.randomUUID().toString();
        String generatedString = uuid;
        String generatedWithoutUnderscore = generatedString.replaceAll("_", "");
        generatedWithoutUnderscore = generatedString.replaceAll("-  ", "");
        String key = ip + "_" + generatedWithoutUnderscore;
        user.setKeyForUnlocking(key);
        em.merge(user);
        em.getTransaction().commit();
        em.close();
    }

    public boolean unlockUser(String email, String key) {
        MaliciousIntentFacade mIF = MaliciousIntentFacade.getMaliciousIntentFacade(emf);
        User user = findUserByUsername(email);
        if(!user.getKeyForUnlocking().equals(key)) {
            return false;
        }
        String[] ipAndKey = key.split("_");
        mIF.liftBan(email, ipAndKey[0]);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        user.setKeyForUnlocking("");
        em.getTransaction().commit();
        em.close();

        return true;
    }

    public void create2FA(String username, String password, String ip) throws AuthenticationException {
        MailSystem ms = new MailSystem();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User user = getVeryfiedUser(username, password, ip);
        String uuid = UUID.randomUUID().toString();
        long ttl = new Date().getTime()+60000;
        user.setTwoFactorCode(uuid + "_" + ttl);
        em.merge(user);
        em.getTransaction().commit();
        em.close();
        ms.twoFactor(user.getUsername(), user.getTwoFactorCode());
    }

    public boolean validate2FA(String username, String twoFactor, String ip) {
        MaliciousIntentFacade mIF = MaliciousIntentFacade.getMaliciousIntentFacade(emf);
        LoginAttempts lA = new LoginAttempts(ip, username, "login");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, username);

        String[] codeAndTtl = twoFactor.split("_");
        long now = new Date().getTime();
        if (Long.parseLong(codeAndTtl[1]) <= now) {
            user.setTwoFactorCode("");
            em.merge(user);
            em.getTransaction().commit();
            em.close();
            return false;
        }
        if(user.getTwoFactorCode().equals(twoFactor)) {
            user.setTwoFactorCode("");
            em.merge(user);
            em.getTransaction().commit();
            em.close();
            return true;
        }
        mIF.logAttempt(lA);
        return false;
    }

    public boolean userPassedValidation(User user) {
        EntityManager em = emf.createEntityManager();
        Date now = new Date();
        if (user.getTtlBeforeDeletion().before(now)) {
            em.remove(user);
            return true;
        }
        return false;
    }

    public boolean verifyUsersMail(String username, String key) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, username);
        em.getTransaction().begin();
        if(user.isVerified()) {
            return true;
        }

        if(!user.getVerificationKey().equals(key)) {
            user.setVerified(false);
            em.merge(user);
            em.getTransaction().commit();
            em.close();
            return false;
        }

        user.setVerified(true);
        user.setVerificationKey("");
        em.merge(user);
        em.getTransaction().commit();
        em.close();
        return true;
    }

    public boolean sendPassResetForUser(String mail) {
        MailSystem ms = new MailSystem();
        EntityManager em = emf.createEntityManager();
        String uuid = UUID.randomUUID().toString();
        String longerUuid = uuid + UUID.randomUUID();
        User user;
        try {
            em.getTransaction().begin();
            user = em.find(User.class, mail);
            user.setResetPassKey(longerUuid);
            em.merge(user);
            em.getTransaction().commit();
            em.close();
            ms.resetPW(mail, longerUuid);
        } catch(NoResultException e) {
            return false;
        }

        return true;
    }
}