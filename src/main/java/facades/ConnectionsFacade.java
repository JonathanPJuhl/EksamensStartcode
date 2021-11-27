package facades;

import entities.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ConnectionsFacade {


    private static EntityManagerFactory emf;
    private static ConnectionsFacade instance;


    private ConnectionsFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static ConnectionsFacade getConnectionsFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ConnectionsFacade();
        }
        return instance;
    }

    public List<UserDTO> getAllConnectionsForUser(String username) {
        EntityManager em = emf.createEntityManager();
        List<String> users;
        try {
            em.getTransaction().begin();
            TypedQuery<String> foundUser = em.createQuery("SELECT c.relation.other_user FROM Connections c WHERE c.relation.this_user = :username AND c.are_friends = true", String.class);
            foundUser.setParameter("username", username);
            em.getTransaction().commit();
            try {
                users = foundUser.getResultList();
            } catch (NoResultException e) {
                return null;
            }

        } finally {
            em.close();
        }
        List<UserDTO> uDTO = new ArrayList<>();
        for (String u : users
        ) {
            uDTO.add(new UserDTO(u));
        }
        return uDTO;
    }

    public Connections sendConnectionRequest(String username, String otherUsersName) {
        EntityManager em = emf.createEntityManager();
        Connections conn;
        try {
            em.getTransaction().begin();
            conn = new Connections(new Relation(username, otherUsersName), false, true);
            em.persist(conn);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return conn;
    }

    public void unSendConnectionRequest(String username, String otherUsersName) {
        EntityManager em = emf.createEntityManager();
        Connections conn;
        try {
            em.getTransaction().begin();
            Relation relation = new Relation(username, otherUsersName);
            conn = em.find(Connections.class, relation);
            em.remove(conn);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void acceptConnectionRequest(String thisUser, String otherUser) {
        EntityManager em = emf.createEntityManager();
        Relation relation = new Relation(thisUser, otherUser);
        em.getTransaction().begin();
        Connections conn = em.find(Connections.class, relation);
        conn.setAre_friends(true);
        em.merge(conn);
        em.getTransaction().commit();
        em.close();
    }

    public void denyConnectionRequest(String thisUser, String otherUser) {
        EntityManager em = emf.createEntityManager();
        Relation relation = new Relation(thisUser, otherUser);
        em.getTransaction().begin();
        Connections conn = em.find(Connections.class, relation);
        em.remove(conn);
        em.getTransaction().commit();
        em.close();
    }

    public List<RelationStatus> GetListOfAllUsersNotBlocked(String thisUser) {
        EntityManager em = emf.createEntityManager();
        List<RelationStatus> rs = new ArrayList<>();
        List<User> tableEmpty = new ArrayList<>();
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        try {
            TypedQuery<User> found = em2.createQuery("SELECT u FROM User u WHERE u.username !=:username", User.class);
            found.setParameter("username", thisUser);
            TypedQuery<Connections> connections = em2.createQuery("SELECT c FROM Connections c WHERE c.relation.other_user != :username", Connections.class);
            connections.setParameter("username", thisUser);
            em2.getTransaction().commit();
            tableEmpty = found.getResultList();
            List<Connections> cList = connections.getResultList();
            for (User u : tableEmpty
            ) {
                if (cList.size() <= 0) {
                    System.out.println("NOT HERE HOPEFULLY");
                    rs.add(new RelationStatus(u.getUsername(), false));
                } else {
                    for (Connections c : cList
                    ) {
                        if (c.getRelation().getOther_user().equals(u.getUsername()) && c.getRelation().getThis_user().equals(thisUser)) {
                            if (!c.isIs_blocked()) {
                                System.out.println("HERE HOPEFULLY 1");
                                rs.add((new RelationStatus(u.getUsername(), c.isRequested())));
                            }
                        } else if (c.getRelation().getThis_user().equals(u.getUsername()) && c.getRelation().getOther_user().equals(thisUser)) {
                            break;
                        }else if (!(c.getRelation().getThis_user().equals(u.getUsername()) && c.getRelation().getOther_user().equals(thisUser))
                        && !(c.getRelation().getOther_user().equals(u.getUsername()) && c.getRelation().getThis_user().equals(thisUser))){
                            System.out.println("HERE HOPEFULLY 2");
                            rs.add(new RelationStatus(u.getUsername(), false));
                        }
                    }
                }
            }
        } catch (NoResultException e) {
            throw new NoResultException("This request could not be fulfilled");
        }
        HashSet<RelationStatus> withUniqueValue = new HashSet<>(rs);
        rs.clear();
        rs.addAll(withUniqueValue);
        em2.close();
        return rs;
    }

    public List<RelationStatus> getPendingConnectionRequests(String thisUser) {
        List<RelationStatus> rs = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Connections> foundConnection = em.createQuery("SELECT c FROM Connections c WHERE c.relation.other_user = :thisUser AND c.requested = true AND c.are_friends=false", Connections.class);
            foundConnection.setParameter("thisUser", thisUser);
            em.getTransaction().commit();
            List<Connections> rst = foundConnection.getResultList();
            for (Connections c : rst
            ) {
                rs.add(new RelationStatus(c.getRelation().getOther_user(), c.isRequested()));
            }
        } finally {
            em.close();
        }
        return rs;
    }
}
