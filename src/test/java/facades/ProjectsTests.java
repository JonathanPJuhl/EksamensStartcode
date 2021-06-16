package facades;

import entities.Developer;
import entities.Proj;
import entities.ProjectHours;
import entities.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectsTests {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    ProjectFacade pF = ProjectFacade.geProjectFacade(emf);


    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);

    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.createQuery("delete from ProjectHours").executeUpdate();
            em.createQuery("delete from Proj").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Developer").executeUpdate();

            Developer developer = new Developer("developer@DEV.DK", 200, "12345678", "kAJ", "usertest" );
            Developer admin = new Developer("admin@admin.dk", 500, "12345679", "Børge", "admintest");
            Proj proj = new Proj("a", "a");
            ProjectHours hours = new ProjectHours("US1", "1st userstory", 200);

            if(admin.getPassword().equals("test")|| developer.getPassword().equals("test")/*||both.getPassword().equals("test")*/)
                throw new UnsupportedOperationException("You have not changed the passwords");

            proj.addProjectHours(hours);
            proj.addDev(developer);
            em.persist(proj);
            Role userRole = new Role("developer");
            Role adminRole = new Role("admin");
            developer.addRole(userRole);
            admin.addRole(adminRole);
            //   both.addRole(userRole);
            hours.setProject(proj);
            em.persist(hours);
            //developer.addProject(proj);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(developer);
            em.persist(admin);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
    @Test
    public void projectShouldBeCreated(){
        pF.addNewProj(new Proj("b", "b"));
        List<Proj> list = pF.listOfAllProjects();
        assertEquals(2, list.size());
    }
    @Test
    public void rightAmountShouldBeReturnedOnSearch(){
        List<Proj> list = pF.listOfAllProjects();
        assertEquals(1, list.size());
        pF.addNewProj(new Proj("b", "b"));
        List<Proj> list2 = pF.listOfAllProjects();
        assertEquals(2, list2.size());
    }
}
