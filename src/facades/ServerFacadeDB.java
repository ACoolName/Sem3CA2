package facades;

import com.google.gson.Gson;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ServerFacadeDB implements ServerFacade {

    EntityManager em;
    Gson gson = new Gson();

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
        Person p = gson.fromJson(json, Person.class);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
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
        return gson.toJson(em.createNamedQuery("Person.findAll", Person.class).getResultList());
    }

    @Override
    public String getPerson(long id) throws NotFoundException {
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new NotFoundException("No person with that id");
        }
        return gson.toJson(p);
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
        p.addRole(r2);
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
    public Course addCourse(String json, long personID, long roleSchoolID, String roleName) throws NotFoundException, InvalidCourseException {
        Course c = gson.fromJson(json, Course.class);
        Person p = em.find(Person.class, personID);
        System.out.println("HERE123");
        for (RoleSchool r : p.getRoles()) {
            if (r.getRoleName().equals(roleName)) {
                switch (roleName) {
                    case "Student": {
                        //RoleSchool temp = em.find(RoleSchool.class, roleSchoolID); //this returns null 
                        Student s = (Student) r;
                        s.addCourse(c);
                        c.addStudent(s);
                        System.out.println("HERE");
                    }
                    break;
                    case "Teacher": {
                        //RoleSchool temp = em.find(Teacher.class, roleSchoolID);
                        Teacher t = (Teacher) r;
                        t.addCourse(c);
                        c.addTeacher(t);
                    }
                    break;
                    case "AssistantTeacher": {
                        //RoleSchool temp = em.find(RoleSchool.class, roleSchoolID);
                        AssistantTeacher as = (AssistantTeacher) r;
                        as.addCourse(c);
                        c.addAssistantTeacher(as);
                    }
                    break;
                    default:
                        break;
                }
            }
        }

        return c;
    }

    @Override
    public String getCourses() {
        return gson.toJson(em.createNamedQuery("Course.findAll", Course.class).getResultList());
    }

}
