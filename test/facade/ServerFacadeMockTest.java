package facade;

import com.google.gson.Gson;
import entity.Person;
import entity.Student;
import exceptions.InvalidRole;
import exceptions.NotFoundException;
import facades.ServerFacadeMock;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServerFacadeMockTest {

    private final Gson gson = new Gson();
    private ServerFacadeMock facade;

    public ServerFacadeMockTest() {
    }

    @Before
    public void setUp() {
        facade = new ServerFacadeMock();
    }

    @Test
    public void testAddAndGetPerson() throws NotFoundException {
        Person p = new Person("a", "b", "c", "d");
        facade.addPerson(gson.toJson(p));
        p = gson.fromJson(facade.getPerson(0), Person.class);
        assertEquals(p.getFirstName(), "a");
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
        facade.addRoleToPerson(gson.toJson(s), 0);
        p = gson.fromJson(facade.getPerson(0), Person.class);
        assertEquals(p.getRoles().get(0).getRoleName(), "Student");
    }

    @After
    public void tearDown() {
    }

}
