package facade;

import com.google.gson.Gson;
import entity.Person;
import entity.Student;
import exceptions.NotFoundException;
import facades.ServerFacadeMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServerFacadeMockTest {
    private final Gson gson = new Gson();
    

    public ServerFacadeMockTest() {
    }

    @Before
    public void setUp() {
    }
    
    @Test
    public void testAddToSpecificClass() throws NotFoundException{
        ServerFacadeMock facade = new ServerFacadeMock();
        
        Person p = new Person("a","b","c","d");
        facade.addPerson(gson.toJson(p));
        Student s = new Student("asddd");
        facade.addRoleToPerson(gson.toJson(s), 0);
        p = gson.fromJson(facade.getPerson(0), Person.class);
        Student s1 = (Student) p.getRoles().get(0);
        assertEquals("asddd",s1.getSemester());
    }

    @After
    public void tearDown() {
    }

}