package utils;


import entities.Developer;
import entities.Project;
import entities.ProjectHours;
import entities.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {
  public void populate(){
    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();


    Developer developer = new Developer("developer", "usertest");
    Developer admin = new Developer("admin", "admintest");
    Developer both = new Developer("user_admin", "bothtest");

    if(admin.getPassword().equals("test")|| developer.getPassword().equals("test")||both.getPassword().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    Role userRole = new Role("developer");
    Role adminRole = new Role("admin");

    em.persist(userRole);
    em.persist(adminRole);
    em.persist(developer);
    developer.addRole(userRole);
    admin.addRole(adminRole);
    both.addRole(userRole);
    both.addRole(adminRole);
    em.getTransaction().commit();
    System.out.println("PW: " + developer.getPassword());
    System.out.println("Testing developer with OK password: " + developer.verifyPassword("test"));
    System.out.println("Testing developer with wrong password: " + developer.verifyPassword("test1"));
    System.out.println("Created TEST Users");
  }

  public static void main(String[] args) {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();

    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    Developer developer = new Developer("developer@DEV.DK", 200, "12345678", "kAJ", "usertest" );
    Developer admin = new Developer("admin@admin.dk", 500, "12345679", "Børge", "admintest");
    Project proj = new Project("a", "a");
    //ProjectHours hours = new ProjectHours("US1", "1st userstory", 200);

    if(admin.getPassword().equals("test")|| developer.getPassword().equals("test")/*||both.getPassword().equals("test")*/)
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    Role userRole = new Role("developer");
    Role adminRole = new Role("admin");

    developer.addRole(userRole);
    admin.addRole(adminRole);
 //   both.addRole(userRole);

   // em.persist(proj);
   // em.persist(hours);


   // proj.addProjectHours(hours);

    //developer.addProject(proj);
    em.persist(userRole);
    em.persist(adminRole);


    em.persist(developer);
    em.persist(admin);


    //em.persist(both);
    em.getTransaction().commit();
    System.out.println("PW: " + developer.getPassword());
    System.out.println("Testing developer with OK password: " + developer.verifyPassword("test"));
    System.out.println("Testing developer with wrong password: " + developer.verifyPassword("test1"));
    System.out.println("Created TEST Users");
   
  }

}
