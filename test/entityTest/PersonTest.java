package entityTest;

import entity.Person;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersonTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Sem3CA2PU");
    
    public PersonTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void personTest() {
        Person p = new Person("Fname", "Lname", "Phone", "Email");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(p);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        Person result = (Person) em.createNamedQuery("Person.findAll").getResultList().get(0);
        String firstP = p.toString().substring(p.toString().indexOf(","));
        String secondP = result.toString().substring(result.toString().indexOf(","));
        assertEquals(firstP, secondP);
        assertTrue(result.getId() != null);
        em.close();
    }
}
