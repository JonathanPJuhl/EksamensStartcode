package facades;

import entities.Developer;
import entities.DeveloperDTO;
import entities.Role;

import javax.enterprise.inject.Typed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
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

    public Developer getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        Developer developer;
        try {
            developer = em.find(Developer.class, username);
            if (developer == null || !developer.verifyPassword(password)) {
                throw new AuthenticationException("Invalid developer name or password");
            }
        } finally {
            em.close();
        }
        return developer;
    }

    public Developer createUser(Developer developer) {

        EntityManager em = emf.createEntityManager();

        Developer userforPersist = new Developer(developer.getEmail(), developer.getPassword());

        em.getTransaction().begin();

        Role userRole = new Role("developer");

        userforPersist.addRole(userRole);

        em.persist(userforPersist);

        em.getTransaction().commit();
        return userforPersist;
    }

    public Developer findUserByUsername(String username){
        EntityManager em = emf.createEntityManager();
        Developer developerFound;
        try {
        em.getTransaction().begin();
        TypedQuery<Developer> user = em.createQuery("SELECT u FROM Developer u WHERE u.email = :username", Developer.class);
        user.setParameter("username", username);
        em.getTransaction().commit();
        developerFound = user.getSingleResult();}
        finally{
        em.close();
        }
        return developerFound;
    }

    public void updatePasswordForUser(Developer developer) {
        EntityManager em = emf.createEntityManager();
        Developer foundDeveloper = findUserByUsername(developer.getEmail());
        foundDeveloper.setPassword(developer.getPassword());
        em.getTransaction().begin();
        em.merge(foundDeveloper);
        em.getTransaction().commit();
        em.close();

    }

    public List<DeveloperDTO> listOfAllDevs() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<String> findDevs = em.createQuery("SELECT d.email FROM Developer d JOIN d.roleList r WHERE r.roleName= :developer", String.class);
        findDevs.setParameter("developer", "developer");
        List<String> foundDevs = findDevs.getResultList();
        List<DeveloperDTO> dto = new ArrayList<>();
        for(String email: foundDevs){
            dto.add(new DeveloperDTO(email));
        }


        return dto;
    }
}

