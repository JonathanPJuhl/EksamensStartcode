package facades;

import entities.LoginAttempts;
import entities.User;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import entities.UserDTO;
import security.RandomString;
import security.errorhandling.AuthenticationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    public List<String> listOfAllUsers() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<String> findUsers = em.createQuery("SELECT u.username FROM User u JOIN u.roleList r WHERE r.roleName= :user", String.class);
        findUsers.setParameter("user", "user");
        List<String> foundUsers = findUsers.getResultList();
        List<String> allUsers = new ArrayList<>();
        allUsers.addAll(foundUsers);
        return allUsers;
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
        byte[] array = new byte[20]; // length is bounded by 7
        new Random().nextBytes(array);
        // charsets not working
        // fix https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
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
}