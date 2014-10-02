package facades;

import com.google.gson.Gson;
import dto.CourseDTO;
import dto.PersonDTO;
import entity.AssistantTeacher;
import entity.Course;
import entity.Person;
import entity.RoleSchool;
import entity.Student;
import entity.Teacher;
import exceptions.InvalidCourseException;
import exceptions.InvalidRole;
import exceptions.NotFoundException;
import interfaces.ServerFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ServerFacadeDB implements ServerFacade {

    private EntityManager em;
    private Gson gson = new Gson();

    public ServerFacadeDB() {
        em = createEntityManager();
    }

    protected ServerFacadeDB(EntityManager em) {
        this.em = em;
    }

    private static EntityManager createEntityManager() {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("Sem3CA2PU");
        return emf.createEntityManager();
    }

    @Override
    public Person addPerson(String json) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        System.out.println(transaction.isActive());
        Person p = gson.fromJson(json, Person.class);

        
        try {
            em.persist(p);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        return p;
    }

    @Override
    public String getPersons() {
        List<Person> people = em.createNamedQuery("Person.findAll",
                Person.class).getResultList();
        List<PersonDTO> dtoPeople = new ArrayList();
        for (Person p : people) {
            dtoPeople.add(new PersonDTO(p));
        }
        return gson.toJson(dtoPeople);
    }

    @Override
    public String getPerson(long id) throws NotFoundException {
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new NotFoundException("No person with that id");
        }
        PersonDTO dto = new PersonDTO(p);
        return gson.toJson(dto);
    }

    @Override
    public RoleSchool addRoleToPerson(String json, long id) throws NotFoundException, InvalidRole {
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new NotFoundException("No person with that id");
        }
        RoleSchool r = gson.fromJson(json, RoleSchool.class);
        RoleSchool r2 = null;
        if (r.getRoleName().equals("Student")) {
            r2 = gson.fromJson(json, Student.class);
        } else if (r.getRoleName().equals("AssistantTeacher")) {
            r2 = gson.fromJson(json, AssistantTeacher.class);
        } else if (r.getRoleName().equals("Teacher")) {
            r2 = gson.fromJson(json, Teacher.class);
        } else {
            throw new InvalidRole(r.getRoleName() + " isn't a valid role");
        }
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(r2);
            p.addRole(r2);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return r2;
    }

    @Override
    public Person deletePerson(long id) throws NotFoundException {
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new NotFoundException("No person with that id");
        }
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.remove(p);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return p;
    }

    @Override
    public Course addPersonToCourse(long courseId, long personId, long roleSchoolId,
            String roleName) throws NotFoundException, InvalidCourseException,
            InvalidRole {
        Course c = em.find(Course.class, courseId);
        Person p = em.find(Person.class, personId);
        RoleSchool r = p.getRole(roleName);
        if(r == null) throw new InvalidRole("Person doesn't have that role");
        switch (roleName) {
            case "Student":
                Student s = em.find(Student.class, r.getId()); //this returns null 
                s.addCourse(c);
                c.addStudent(s);
                break;
            case "Teacher":
                Teacher t = em.find(Teacher.class, roleSchoolId);
                t.addCourse(c);
                c.addTeacher(t);
                break;
            case "AssistantTeacher":
                AssistantTeacher as = em.find(AssistantTeacher.class, roleSchoolId);
                as.addCourse(c);
                c.addAssistantTeacher(as);
                break;
            default:
                break;
        }
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return c;
    }

    @Override
    public Course addCourse(String json) throws NotFoundException, InvalidCourseException {
        Course c = gson.fromJson(json, Course.class);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(c);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

        return c;
    }

    @Override
    public String getCourses() {
        List<Course> courses = em.createNamedQuery("Course.findAll",
                Course.class).getResultList();
        List<CourseDTO> dtoCourses = new ArrayList();
        for (Course c : courses) {
            dtoCourses.add(new CourseDTO(c));
        }
        return gson.toJson(dtoCourses);
    }

    @Override
    public String getRole(long personId, String roleName) throws InvalidRole {
        if(isValidRole(roleName))
            throw new InvalidRole(roleName + " is invalid role");
        Person person = em.find(Person.class, personId);
        RoleSchool r = person.getRole(roleName);
        if(r == null) throw new InvalidRole("Person doesn't have that role");
        return gson.toJson(r);
    }

    @Override
    public String getRoles(long personId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean isValidRole(String roleName) {
        return roleName.equals("Student") || roleName.equals("Teacher") || 
                roleName.equals("AssistantTeacher");
    }

}
