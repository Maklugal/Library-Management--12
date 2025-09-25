package com.mak.library.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");
    public static EntityManager em() { return emf.createEntityManager(); }
    public static void close() { emf.close(); }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
