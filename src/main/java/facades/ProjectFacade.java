package facades;

import entities.Developer;
import entities.Proj;
import entities.Project;
import entities.Role;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 * @author lam@cphbusiness.dk
 */
public class ProjectFacade {

    private static EntityManagerFactory emf;
    private static ProjectFacade instance;

    private ProjectFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static ProjectFacade geProjectFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ProjectFacade();
        }
        return instance;
    }

    public Proj addNewProj(Proj project){
        System.out.println(project);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(project);
        em.getTransaction().commit();
        em.close();
        return project;
    }
}

