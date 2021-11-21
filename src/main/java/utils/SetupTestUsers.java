package utils;


import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {
    public void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        User endUser = new User("developer@DEV.DK", "usertest");

        em.getTransaction().begin();

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

        endUser.addRole(userRole);

        em.persist(userRole);
        em.persist(adminRole);

        //em.persist(admin);
        em.persist(endUser);

        em.getTransaction().commit();
        System.out.println("Created TEST Users");
    }

    public static void main(String[] args) {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        User endUser = new User("developer@DEV.DK", "usertest");

        em.getTransaction().begin();

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

        endUser.addRole(userRole);

        em.persist(userRole);
        em.persist(adminRole);

        //em.persist(admin);
        em.persist(endUser);

        em.getTransaction().commit();
        System.out.println("Created TEST Users");
    }

}
