package facades;

import entities.*;
import security.errorhandling.AuthenticationException;

import javax.persistence.*;
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

    public void addHoursToProj(HourRecorderDTO dto) {
        EntityManager em = emf.createEntityManager();
       em.getTransaction().begin();
       try {
           TypedQuery<ProjectHours> addTime = em.createQuery("SELECT ph from ProjectHours ph join Proj p WHERE p.name=:projectName AND ph.userStory = :userStory AND ph.dev.email=:email", ProjectHours.class);
           addTime.setParameter("email", dto.getEmail());
           addTime.setParameter("projectName", dto.getProjectName());
           addTime.setParameter("userStory", dto.getUserStory());
           ProjectHours ph = addTime.getSingleResult();
           ph.setHoursSpent(ph.getHoursSpent() + dto.getHoursSpent());
           em.merge(ph);
           em.getTransaction().commit();
       }catch(NoResultException e){
           ProjectHours ph = new ProjectHours(dto.getUserStory(), dto.getDescription(), dto.getHoursSpent());
           Proj proj = em.find(Proj.class, dto.getProjectName());
           ph.setProject(proj);
           ph.setDev(em.find(Developer.class, dto.getEmail()));
           em.persist(ph);
           em.getTransaction().commit();
       } finally {
           em.close();
       }
    }

    public UserStoryHourDTO getAllUserstoriesForGivenProject(String projectName) {
        EntityManager em = emf.createEntityManager();
        //TypedQuery<ProjectHours> findStories = em.createQuery("SELECT p FROM ProjectHours p join Proj pp WHERE pp.name=:projectName", ProjectHours.class);
        TypedQuery<String> story = em.createQuery("SELECT h.userStory from Proj p JOIN p.projectHours h WHERE p.name=:projectName", String.class);
        story.setParameter("projectName", projectName);
      //  System.out.println(findStories.toString());
        //findStories.setParameter("projectName", projectName);
        //List<ProjectHours> foundStories = findStories.getResultList();
        List<String> foundStories = story.getResultList();
        System.out.println("SIZE: " + foundStories.size());
        UserStoryHourDTO stories = new UserStoryHourDTO(null);
        /*for(int i=0; i<foundStories.size(); i++){
            System.out.println(foundStories.get(i));
            stories.addUserStories(foundStories.get(i));
        }*/
        stories.setUserStories(foundStories);

        return stories;
    }

    public Double getHoursSpentOnUserstories(String developer, String project) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<ProjectHours> foundHours = em.createQuery("SELECT ph FROM ProjectHours ph WHERE ph.project.name=:projectName AND ph.dev.email= :devEmail", ProjectHours.class);
        foundHours.setParameter("projectName", project);
        foundHours.setParameter("devEmail", developer);
        List<ProjectHours> finalList = foundHours.getResultList();
        double totalHours = 0.0;
        for(int i =0; i<finalList.size(); i++){
            totalHours+=finalList.get(i).getHoursSpent();
        }
        return totalHours;
    }

    public List<ProjectHours> getInvoice(String project) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<ProjectHours> createInvoice = em.createQuery("SELECT ph FROM ProjectHours ph WHERE ph.project.name=:project", ProjectHours.class);
        createInvoice.setParameter("project", project);
        List<ProjectHours> invoice = createInvoice.getResultList();
        return invoice;
    }
}

