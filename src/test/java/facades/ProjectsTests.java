//package facades;
//
//import entities.*;
//import org.eclipse.persistence.sessions.Project;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import utils.EMF_Creator;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.TypedQuery;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class ProjectsTests {
//
//    private static EntityManagerFactory emf;
//    private static UserFacade facade;
//    ProjectFacade pF = ProjectFacade.geProjectFacade(emf);
//
//
//    @BeforeAll
//    public static void setUpClass() {
//        emf = EMF_Creator.createEntityManagerFactoryForTest();
//        facade = UserFacade.getUserFacade(emf);
//
//    }
//
//    @AfterAll
//    public static void tearDownClass() {
////        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
//    }
//
//    // Setup the DataBase in a known state BEFORE EACH TEST
//    //TODO -- Make sure to change the code below to use YOUR OWN entity class
//    @BeforeEach
//    public void setUp() {
//        EntityManager em = emf.createEntityManager();
//
//        try {
//            em.getTransaction().begin();
//
//
//
//            em.createQuery("delete from ProjectHours").executeUpdate();
//            em.createQuery("delete from Proj").executeUpdate();
//            em.createQuery("delete from Role").executeUpdate();
//            em.createQuery("delete from Developer").executeUpdate();
//
//
//            Developer developer = new Developer("developer@DEV.DK", 200, "12345678", "kAJ", "usertest" );
//            Developer admin = new Developer("admin@admin.dk", 500, "12345679", "Børge", "admintest");
//            Proj proj = new Proj("a", "a");
//            ProjectHours hours = new ProjectHours("US1", "1st userstory", 200);
//
//            if(admin.getPassword().equals("test")|| developer.getPassword().equals("test")/*||both.getPassword().equals("test")*/)
//                throw new UnsupportedOperationException("You have not changed the passwords");
//
//            proj.addProjectHours(hours);
//            proj.addDev(developer);
//            em.persist(proj);
//            Role userRole = new Role("developer");
//            Role adminRole = new Role("admin");
//            developer.addRole(userRole);
//            admin.addRole(adminRole);
//            //   both.addRole(userRole);
//            hours.setProject(proj);
//            hours.setDev(developer);
//            em.persist(hours);
//            //developer.addProject(proj);
//            em.persist(userRole);
//            em.persist(adminRole);
//            em.persist(developer);
//            em.persist(admin);
//            em.getTransaction().commit();
//
//        } finally {
//            em.close();
//        }
//    }
//    @Test
//    public void projectShouldBeCreated(){
//        pF.addNewProj(new Proj("b", "b"));
//        List<Proj> list = pF.listOfAllProjects();
//        assertEquals(2, list.size());
//    }
//    @Test
//    public void rightAmountShouldBeReturnedOnSearch(){
//        List<Proj> list = pF.listOfAllProjects();
//        assertEquals(1, list.size());
//        pF.addNewProj(new Proj("b", "b"));
//        List<Proj> list2 = pF.listOfAllProjects();
//        assertEquals(2, list2.size());
//    }
//    @Test
//    public void devShouldBeAssignedToProject(){
//        Proj proj = new Proj("b", "b");
//        EntityManager em = emf.createEntityManager();
//        EntityManager em2 = emf.createEntityManager();
//        em.getTransaction().begin();
//        em.persist(proj);
//        em.getTransaction().commit();
//        em.close();
//
//
//        em2.getTransaction().begin();
//        Developer dev = em2.find(Developer.class, "developer@DEV.DK");
//        pF.assignDevToProject(dev.getEmail(), "b");
//
//        Proj found = em2.find(Proj.class, "b");
//        em2.getTransaction().commit();
//        em2.close();
//        assertEquals(found.getDevList().get(0).getDevName(), dev.getDevName());
//
//    }
//    @Test
//    public void allUserStoriesShouldBeReturned(){
//        ProjectHours hours = new ProjectHours("US2", "2nd userstory", 200);
//        ProjectHours hours1 = new ProjectHours("US3", "3rd userstory", 200);
//        ProjectHours hours2 = new ProjectHours("US4", "4th userstory", 200);
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        Proj proj = em.find(Proj.class, "a");
//        proj.addProjectHours(hours);
//        proj.addProjectHours(hours1);
//        proj.addProjectHours(hours2);
//        em.merge(proj);
//        em.getTransaction().commit();
//        UserStoryHourDTO dto = pF.getAllUserstoriesForGivenProject("a");
//        assertEquals(dto.getUserStories().size(), 4);
//
//        em.close();
//
//    }
//    @Test
//    public void timeSpentShouldBeaddedToExisting(){
//
//        HourRecorderDTO dto = new HourRecorderDTO("developer@DEV.DK", "a", 400, "US1");
//        pF.addHoursToProj(dto);
//        EntityManager em = emf.createEntityManager();
//        ProjectHours pH = em.find(ProjectHours.class, "US1");
//        assertEquals(pH.getHoursSpent(), 600);
//    }
//    @Test
//    public void timeSpentShouldBeaddedToNew(){
//
//        HourRecorderDTO dto = new HourRecorderDTO("developer@DEV.DK", "a", 400, "US2", "2nd");
//        pF.addHoursToProj(dto);
//        EntityManager em = emf.createEntityManager();
//        ProjectHours pH = em.find(ProjectHours.class, "US2");
//        assertEquals(pH.getHoursSpent(), 400);
//    }
//    @Test
//    public void timeSpentShouldBeShownCorrectly(){
//
//        HourRecorderDTO dto = new HourRecorderDTO("developer@DEV.DK", "a", 400, "US1");
//        HourRecorderDTO dto2 = new HourRecorderDTO("developer@DEV.DK", "a", 400, "US2", "2nd");
//        pF.addHoursToProj(dto);
//        pF.addHoursToProj(dto2);
//        EntityManager em = emf.createEntityManager();
//        assertEquals(pF.getHoursSpentOnUserstories("developer@DEV.DK", "a"), 1000);
//
//    }
//    @Test
//    public void invoiceShouldBeShownCorrectly(){
//
//        HourRecorderDTO dto = new HourRecorderDTO("developer@DEV.DK", "a", 400, "US1");
//        HourRecorderDTO dto2 = new HourRecorderDTO("developer@DEV.DK", "a", 400, "US2", "2nd");
//        pF.addHoursToProj(dto);
//        pF.addHoursToProj(dto2);
//
//        assertEquals(pF.getInvoice("a").size(), 2);
//        double hours = 0.0;
//        for(int i = 0; i< pF.getInvoice("a").size(); i++){
//            hours += pF.getInvoice("a").get(i).getHours();
//        }
//        assertEquals(hours, 1000);
//    }
//}
