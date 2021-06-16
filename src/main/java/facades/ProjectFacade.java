package facades;

import entities.*;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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
    public List<Proj> listOfAllProjects(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Proj> findAll = em.createQuery("SELECT p FROM Proj p", Proj.class);
        List<Proj> found = findAll.getResultList();
        return found;

    }
    public List<ProjDTO> listAsDTO(List<Proj> projs){
        List<ProjDTO> list = new ArrayList<>();
        for(Proj proj: projs){
            list.add(new ProjDTO(proj.getName(), proj.getDescription()));
        }
        return list;
    }
}

