package facades;

import entities.ActiveUsersDTO;
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

    public List<ActiveUsersDTO> listOfAllUsers() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> findUsers = em.createQuery("SELECT u FROM User u JOIN u.roleList r WHERE r.roleName= :user", User.class);
        findUsers.setParameter("user", "user");
        List<User> foundUsers = findUsers.getResultList();

        List<ActiveUsersDTO> aUD = new ArrayList<>();
        for (User u: foundUsers
             ) {
             aUD.add(new ActiveUsersDTO(u.getUsername(), u.isDeleted()));
        }
        return aUD;
    }

}
