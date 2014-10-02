package facades;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.Course;
import entity.Person;
import entity.Student;
import exceptions.InvalidCourseException;
import exceptions.InvalidRole;
import exceptions.NotFoundException;
import facades.ServerFacadeDB;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class ServerFacadeDBTest {

    private final Gson gson = new Gson();
    private static ServerFacadeDB facade;
    private static EntityManager em;

    public ServerFacadeDBTest() {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("Sem3CA2PU");
        em = emf.createEntityManager();
        facade = new ServerFacadeDB(em);
    }

    @BeforeClass
    public static void init() {
    }

    @Before
    public void setUp() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("DELETE FROM PERSON_ROLESCHOOL").executeUpdate();
        em.createNativeQuery("DELETE FROM COURSE").executeUpdate();
        em.createNativeQuery("DELETE FROM ROLESCHOOL").executeUpdate();
        em.createNativeQuery("DELETE FROM PERSON").executeUpdate();
        transaction.commit();
    }

    @Test
    public void testAddAndGetPerson() throws NotFoundException {
        Person p = new Person("a", "b", "c", "d");
        facade.addPerson(gson.toJson(p));
        List<Person> persons = gson.fromJson(facade.getPersons(),
                new TypeToken<List<Person>>() {
                }.getType());
        assertEquals(persons.get(0).getFirstName(), "a");
    }

    @Test
    public void testGetPersons() {
        Person p = new Person("a", "b", "c", "d");
        Person person1 = facade.addPerson(gson.toJson(p));
        Person p2 = new Person("a2", "b2", "c2", "d2");
        Person person2 = facade.addPerson(gson.toJson(p2));

        Map<Long, Person> test = new HashMap();
        test.put(person1.getId(), person1);
        test.put(person2.getId(), person2);
        String expected = gson.toJson(test.values());
        String result = facade.getPersons();
        System.out.println(expected);
        System.out.println(result);
        assertEquals(expected, result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNonExistingPerson() throws Exception {
        facade.getPerson(999);
    }

    @Test(expected = NotFoundException.class)
    public void testDeletePerson() throws NotFoundException {
        Person p = new Person("a", "b", "c", "d");
        Person person = facade.addPerson(gson.toJson(p));
        facade.deletePerson(person.getId());
        facade.getPerson(person.getId());
    }

    @Test
    public void testAddRoleToPerson() throws NotFoundException, InvalidRole {
        Person p = new Person("a", "b", "c", "d");
        facade.addPerson(gson.toJson(p));
        Student s = new Student("asddd");
        List<Person> persons = gson.fromJson(facade.getPersons(),
                new TypeToken<List<Person>>() {
                }.getType());
        facade.addRoleToPerson(gson.toJson(s), persons.get(0).getId());
        persons = gson.fromJson(facade.getPersons(),
                new TypeToken<List<Person>>() {
                }.getType());
        assertEquals(persons.get(0).getRoles().get(0).getRoleName(), "Student");
    }

    @Test
    public void testAddCourse() throws NotFoundException, InvalidCourseException, InvalidRole {
        Person p = new Person("a", "b", "c", "d");
        facade.addPerson(gson.toJson(p));
        Student s = new Student("asddd");
        List<Person> persons = gson.fromJson(facade.getPersons(),
                new TypeToken<List<Person>>() {
                }.getType());
        facade.addRoleToPerson(gson.toJson(s), persons.get(0).getId());
        persons = gson.fromJson(facade.getPersons(),
                new TypeToken<List<Person>>() {
                }.getType());
        System.out.println(persons.get(0).getRoles().get(0));
        Course c = new Course("lol", "THIS IS NOT WORKING!");
        c.setId(23l);
        facade.addCourse(gson.toJson(c), persons.get(0).getId(), persons.get(0).getRoles().get(0).getId(), "Student"); //This needs to be revised ! 
        List<Course> courses = gson.fromJson(facade.getCourses(), new TypeToken<List<Course>>() {
        }.getType());
        System.out.println(courses.size());
        //gets size of courses = 0
        assertEquals(courses.get(0).getStudents().get(0).getRoleName(), "Student");
    }

    @After
    public void tearDown() {
    }

}
