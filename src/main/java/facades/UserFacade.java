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

        if(mIF.isBanned(lA)) {
            throw new AuthenticationException("Your Ip is banned, try again in 10 hours");
        }
        if(mIF.getLoggedAttempts(lA) >= 3) {
            mIF.createBan(lA);
            throw new AuthenticationException("Too many login attempts, try again in 10 hours");
        }
        try {
            if(endUser != null && !endUser.verifyPassword(password)) {
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

        em.getTransaction().begin();

        Role userRole = new Role("user");

        userforPersist.addRole(userRole);

        em.persist(userforPersist);

        em.getTransaction().commit();
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

    public void updatePasswordForUser(User user) {
        EntityManager em = emf.createEntityManager();
        User foundUser = findUserByUsername(user.getUsername());
        foundUser.setPassword(user.getPassword());
        em.getTransaction().begin();
        em.merge(foundUser);
        em.getTransaction().commit();
        em.close();

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
        System.out.println(user);
        em.getTransaction().commit();
        em.close();
        ms.twoFactor(user.getUsername(), user.getTwoFactorCode());
    }

    public boolean validate2FA(String username, String twoFactor) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, username);

        String[] codeAndTtl = twoFactor.split("_");
        long now = new Date().getTime();
        if (Long.parseLong(codeAndTtl[1]) <= now) {
            System.out.println("UDLØBET");
            user.setTwoFactorCode("");
            em.merge(user);
            em.getTransaction().commit();
            em.close();
            return false;
        }
        System.out.println("USER CODE:" + user.getTwoFactorCode());
        System.out.println("Supplied:" + twoFactor);
        if(user.getTwoFactorCode().equals(twoFactor)) {
            System.out.println("SUCCESS");
            user.setTwoFactorCode("");
            em.merge(user);
            em.getTransaction().commit();
            em.close();
            return true;
        }
        return false;
    }
}