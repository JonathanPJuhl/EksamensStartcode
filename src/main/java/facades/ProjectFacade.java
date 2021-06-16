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
    public Proj findProjByName(String projectName){
        EntityManager em = emf.createEntityManager();
        Proj projFound;
        try {
            em.getTransaction().begin();
            TypedQuery<Proj> user = em.createQuery("SELECT p FROM Proj p WHERE p.name = :name", Proj.class);
            user.setParameter("name", projectName);
            em.getTransaction().commit();
            projFound = user.getSingleResult();}
        finally{
            em.close();
        }
        return projFound;
    }

    public void assignDevToProject(String dev, String proj) {
        EntityManager em = emf.createEntityManager();
        UserFacade uF = UserFacade.getUserFacade(emf);
        Developer developer = uF.findUserByUsername(dev);
        Proj project = findProjByName(proj);
        project.addDev(developer);
        em.getTransaction().begin();
        em.merge(project);
        em.getTransaction().commit();
        em.close();
    }
}

