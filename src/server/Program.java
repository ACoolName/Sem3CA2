package server;

import entity.Course;
import entity.Person;
import entity.RoleSchool;
import entity.Student;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Program {

    private static EntityManager em;

    private static EntityManager createEntityManager() {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("Sem3CA2PU");
        return emf.createEntityManager();
    }

    private static void addPeople() {

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Person p = new Person("addd", "bbbbb", "cccc", "dddd");
        em.persist(p);
        System.out.println("dad");
        Student s = new Student("first");
        em.persist(s);
        p.addRole(s);
        transaction.commit();
        
//            transaction = em.getTransaction();
//            transaction.begin();
//            p = em.find(Person.class, 1L);
//            p.addRole(s);
//            em.persist(p);
//            transaction.commit();
//            transaction = em.getTransaction();
//            transaction.begin();
//            List<Person> people = em.createQuery("Person.findAll").getResultList();
//            System.out.println(people.size());
//            transaction.commit();
        System.out.println("hallds");
    }

    public static void main(String[] args) {
        em = createEntityManager();
        addPeople();
        addCourse();
        em.close();
    }

    private static void addCourse() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Course c = new Course("Gay course", "Only teo allowed");
        em.persist(c);
        List<Person> people = em.createNamedQuery("Person.findAll").getResultList();
        Student s = em.find(Student.class, people.get(0).getRole("Student").getId());
        s.addCourse(c);
        c.addStudent(s);
        transaction.commit();

//        EntityTransaction transaction = em.getTransaction();
        List<Course> courses = em.createNamedQuery("Course.findAll").getResultList();
        System.out.println(courses.get(0));
        Person p = em.find(Person.class, 1L);
        
        System.out.println(p.getRole("Student"));
    }

}
