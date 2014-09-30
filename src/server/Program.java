package server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Program {

    private static EntityManager createEntityManager() {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("Sem3CA2PU");
        return emf.createEntityManager();
    }

    private static void testJpaTables() {
        EntityManager em = createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
            em.close();
    }

    public static void main(String[] args) {
        testJpaTables();
    }

}
