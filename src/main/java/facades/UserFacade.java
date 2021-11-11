package facades;

import entities.User;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
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

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User endUser;
        try {
            endUser = em.find(User.class, username);
            if (endUser == null || !endUser.verifyPassword(password)) {
                throw new AuthenticationException("Invalid username or password");
            }
        } finally {
            em.close();
        }
        return endUser;
    }

//    public User getVeryfiedDeveloper(String username, String password) throws AuthenticationException {
//        EntityManager em = emf.createEntityManager();
//        Developer developer;
//        try {
//            developer = em.find(Developer.class, username);
//            if (developer == null || !developer.verifyPassword(password)) {
//                throw new AuthenticationException("Invalid developer name or password");
//            }
//        } finally {
//            em.close();
//        }
//        return developer;
//    }

    public User createUser(User endUser) {

        EntityManager em = emf.createEntityManager();

        User userforPersist = new User(endUser.getUsername(), endUser.getPassword(), endUser.getRecoveryquestion(), endUser.getAnswer());

        em.getTransaction().begin();

        Role userRole = new Role("developer");

        userforPersist.addRole(userRole);

        em.persist(userforPersist);

        em.getTransaction().commit();
        return userforPersist;
    }
//    public Developer createDeveloper(Developer developer) {
//
//        EntityManager em = emf.createEntityManager();
//
//        Developer userforPersist = new Developer(developer.getEmail(), developer.getPassword());
//
//        em.getTransaction().begin();
//
//        Role userRole = new Role("developer");
//
//        userforPersist.addRole(userRole);
//
//        em.persist(userforPersist);
//
//        em.getTransaction().commit();
//        return userforPersist;
//    }

    public User findUserByUsername(String username){
        EntityManager em = emf.createEntityManager();
        User user;
        try {
        em.getTransaction().begin();
        TypedQuery<User> foundUser = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        foundUser.setParameter("username", username);
        em.getTransaction().commit();
        user = foundUser.getSingleResult();}
        finally{
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
}

