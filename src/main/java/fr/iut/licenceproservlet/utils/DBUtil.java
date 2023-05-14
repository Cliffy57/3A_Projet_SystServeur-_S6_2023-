package fr.iut.licenceproservlet.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUtil {

    private static EntityManagerFactory entityManagerFactory;

    // Méthode pour initialiser l'EntityManagerFactory
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("appointments");
    }

    // Méthode pour récupérer un EntityManager à partir de l'EntityManagerFactory
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    // Méthode pour fermer l'EntityManagerFactory
    public static void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
