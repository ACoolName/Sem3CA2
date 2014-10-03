package facades;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.CourseDTO;
import dto.PersonDTO;
import dto.RoleSchoolDTO;
import entity.Course;
import entity.Person;
import entity.RoleSchool;
import entity.Student;
import entity.Teacher;
import exceptions.InvalidCourseException;
import exceptions.InvalidRole;
import exceptions.NotFoundException;
import facades.ServerFacadeDB;
import java.util.ArrayList;
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
                = Persistence.createEntityManagerFactory("Sem3CA2PU"); //Add your persistant name here!!
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
        em.createNativeQuery("DELETE FROM COURSE_ASSTEACHER").executeUpdate();
        em.createNativeQuery("DELETE FROM COURSE_TEACHER").executeUpdate();
        em.createNativeQuery("DELETE FROM COURSE_STUDENT").executeUpdate();
        em.createNativeQuery("DELETE FROM PERSON_ROLESCHOOL").executeUpdate();
        em.createNativeQuery("DELETE FROM STUDENT").executeUpdate();
        em.createNativeQuery("DELETE FROM TEACHER").executeUpdate();
        em.createNativeQuery("DELETE FROM ASSISTANTTEACHER").executeUpdate();
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

        Map<Long, PersonDTO> test = new HashMap();
        test.put(person1.getId(), new PersonDTO(person1));
        test.put(person2.getId(), new PersonDTO(person2));
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
    public void testAddAndGetRoleToPerson() throws NotFoundException, InvalidRole {
        Person p = new Person("a", "b", "c", "d");
        p = facade.addPerson(gson.toJson(p));
        Student s = new Student("asddd");
        facade.addRoleToPerson(gson.toJson(s), p.getId());
        Student student = gson.fromJson(facade.getRole(p.getId(),
                "Student"), Student.class);
        assertEquals(student.getRoleName(), "Student"); //working !!! 
    }

    @Test
    public void testAddAndGetCourse() {
        Course c = new Course("lol", "THIS IS NOT WORKING!");
        c = facade.addCourse(gson.toJson(c)); //This needs to be revised ! Not anymore :)
        List<Course> courses = gson.fromJson(facade.getCourses(),
                new TypeToken<List<Course>>() {
                }.getType());
        assertEquals(courses.get(0).getDescription(), c.getDescription());
    }

    @Test
    public void testGetCourseById() throws InvalidCourseException {
        Course c = new Course("Single Course", "A good description");
        c = facade.addCourse(gson.toJson(c));
        c = gson.fromJson(facade.getCourse(c.getId()), Course.class);
        assertEquals(c.getDescription(), c.getDescription());
    }

    @Test(expected = InvalidCourseException.class)
    public void testGetCourseByIdNoSuchId() throws InvalidCourseException {
        facade.getCourse(9999);
    }

    @Test(expected = InvalidRole.class)
    public void testGetCoursesByRoleIdInvalidRole() throws InvalidCourseException,
            InvalidRole, NotFoundException {
        final long IRRELEVANT = 0;
        facade.getCourses(IRRELEVANT, "Professor");
    }

    @Test(expected = NotFoundException.class)
    public void testGetCoursesByRoleIdNoSuchPerson() throws InvalidCourseException,
            InvalidRole, NotFoundException {
        facade.getCourses(99999, "Student");
    }

    @Test
    public void testGetCoursesWithRoleId() throws InvalidRole, NotFoundException, InvalidCourseException {
        Person p = new Person("a", "b", "c", "d");
        p = facade.addPerson(gson.toJson(p));
        Student s = new Student("asddd");
        RoleSchool student = facade.addRoleToPerson(gson.toJson(s), p.getId());
        Course c = new Course("Best Course", "Tobias course");
        c = facade.addCourse(gson.toJson(c));
        facade.addPersonToCourse(c.getId(), p.getId(), student.getId(), s.getRoleName());
        List<CourseDTO> courses = gson.fromJson(facade.getCourses(student.getId(), "Student"),
                new TypeToken<List<CourseDTO>>() {
                }.getType());
        assertEquals(courses.get(0).getDescription(), c.getDescription());
    }
    
    @Test
    public void testGetRole() throws InvalidRole, NotFoundException {
        Person p = new Person("a", "b", "c", "d");
        p = facade.addPerson(gson.toJson(p));
        facade.addRoleToPerson(gson.toJson(new Student("first")), p.getId());
        Student s = gson.fromJson(facade.getRole(p.getId(), "Student"), Student.class);
        assertEquals(s.getSemester(), "first");
    }

    @Test(expected = NotFoundException.class)
    public void testGetRoleNoSuchPerson() throws InvalidRole, NotFoundException {
        facade.getRole(9999, "Student");
    }

    @Test(expected = InvalidRole.class)
    public void testGetRoleInvalidRole() throws InvalidRole, NotFoundException {
        facade.getRole(0L, "Slacker");
    }

    @Test(expected = InvalidRole.class)
    public void testGetRoleNoSuchRoleInPerson() throws InvalidRole, NotFoundException {
        Person p = new Person("a", "b", "c", "d");
        p = facade.addPerson(gson.toJson(p));
        facade.addRoleToPerson(gson.toJson(new Student("first")), p.getId());
        facade.getRole(p.getId(), "Teacher");
    }

    @Test(expected = NotFoundException.class)
    public void testAddRoleNoSuchPerson() throws InvalidRole, NotFoundException {
        Student s = new Student("asddd");
        facade.addRoleToPerson(gson.toJson(s), 9999);
    }

    @Test
    public void testGetCourseNoCourses() {
        List<Course> courses = new ArrayList();
        String expected = gson.toJson(courses);
        String received = facade.getCourses();
        assertEquals(received, expected);
    }

    @Test
    public void testAddPersonToCourse() throws NotFoundException, InvalidCourseException, InvalidRole {
        Person p = new Person("a", "b", "c", "d");
        p = facade.addPerson(gson.toJson(p));
        Student s = new Student("asddd");
        RoleSchool student = facade.addRoleToPerson(gson.toJson(s), p.getId());
        Course c = new Course("lol", "THIS IS NOT WORKING!");
        c = facade.addCourse(gson.toJson(c)); //This needs to be revised ! Not anymore :)
        facade.addPersonToCourse(c.getId(), p.getId(), student.getId(), s.getRoleName());
        List<Person> students = gson.fromJson(facade.getStudentsInCourse(c.getId()),
                new TypeToken<List<Person>>() {
                }.getType());
        s = gson.fromJson(facade.getRole(students.get(0).getId(), "Student"), Student.class);
        assertEquals(c.getStudents().get(0).getId(), s.getId());
    }

    @Test
    public void testGetRoles() throws NotFoundException, InvalidRole {
        Person p = new Person("a", "b", "c", "d");
        p = facade.addPerson(gson.toJson(p));
        Student s = new Student("asddd");
        RoleSchool student = facade.addRoleToPerson(gson.toJson(s), p.getId());
        List<RoleSchoolDTO> roles = gson.fromJson(facade.getRoles(p.getId()),
                new TypeToken<List<RoleSchoolDTO>>() {
                }.getType());
        assertEquals(roles.size(), 1);
    }

    @Test(expected = NotFoundException.class)
    public void testGetRolesNoSuchPerson() throws NotFoundException, InvalidRole {
        facade.getRoles(9999);
    }

    @Test
    public void testGetRolesNoRoles() throws NotFoundException, InvalidRole {
        Person p = new Person("a", "b", "c", "d");
        p = facade.addPerson(gson.toJson(p));
        List<RoleSchoolDTO> roles = new ArrayList();
        assertEquals(facade.getRoles(p.getId()), gson.toJson(roles));
    }

    @Test(expected = NotFoundException.class)
    public void testGetStudentsNoSuchPerson() throws NotFoundException {
        facade.getStudentsInCourse(9999);
    }

    @Test(expected = NotFoundException.class)
    public void testGetTeachersNoSuchPerson() throws NotFoundException {
        facade.getTeachersInCourse(9999);
    }

    @Test(expected = NotFoundException.class)
    public void testGetAssistantTeachersNoSuchPerson() throws NotFoundException {
        facade.getAssistantTeachersInCourse(9999);
    }

    @After
    public void tearDown() {
    }

}
