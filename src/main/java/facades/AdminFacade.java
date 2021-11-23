package facades;

import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class AdminFacade {

    private static EntityManagerFactory emf;
    private static AdminFacade instance;


    private AdminFacade() {
    }

    public static AdminFacade getAdminFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdminFacade();
        }
        return instance;
    }

    public void deActivateUser(String username) throws NoResultException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<User> foundUser = em.createQuery("Update User u SET u.isDeleted = 1 WHERE u.username = :username", User.class);
            foundUser.setParameter("username", username);
            foundUser.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    public void reActivateUser(String username) throws NoResultException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<User> foundUser = em.createQuery("Update User u SET u.isDeleted = 0 WHERE u.username = :username", User.class);
            foundUser.setParameter("username", username);
            foundUser.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<String> listOfAllUsers() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<String> findUsers = em.createQuery("SELECT u.username FROM User u JOIN u.roleList r WHERE r.roleName= :user AND u.isDeleted = false", String.class);
        findUsers.setParameter("user", "user");
        List<String> foundUsers = findUsers.getResultList();
        List<String> allUsers = new ArrayList<>();
        allUsers.addAll(foundUsers);
        return allUsers;
    }

}
