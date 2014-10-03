package facades;

import com.google.gson.Gson;
import dto.AssistantTeacherDTO;
import dto.CourseDTO;
import dto.PersonDTO;
import dto.RoleSchoolDTO;
import dto.StudentDTO;
import dto.TeacherDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Person p = gson.fromJson(json, Person.class);

        try {
            em.persist(p);
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        return gson.toJson(dtoPeople); // this is not right ! 
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
        System.out.println("addRoleToPerson: " + json);
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new NotFoundException("No person with that id");
        }
        RoleSchool r = gson.fromJson(json, RoleSchool.class);
        System.out.println("role school: " + r);
        RoleSchool r2 = null;
        if (r.getRoleName().equals("Student")) {
            r2 = gson.fromJson(json, Student.class);
        } else if (r.getRoleName().equals("AssistantTeacher")) {
            r2 = gson.fromJson(json, AssistantTeacher.class);
        } else if (r.getRoleName().equals("Teacher")) {
            System.out.println("role in if");
            r2 = gson.fromJson(json, Teacher.class);
        } else {
            throw new InvalidRole(r.getRoleName() + " isn't a valid role");
        }
        System.out.println("role before adding: " + r2);
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
    public String addPersonToCourse(long courseId, long personId, long roleSchoolId,
            String roleName) throws NotFoundException, InvalidCourseException,
            InvalidRole {
        Course c = em.find(Course.class, courseId);
        if (c == null) {
            throw new InvalidCourseException("No such course");
        }
        Person p = em.find(Person.class, personId);
        if (p == null) {
            throw new NotFoundException("No person with that id");
        }
        RoleSchool r = p.getRole(roleName);
        if (r == null) {
            throw new InvalidRole("Person doesn't have that role");
        }
        switch (roleName) {
            case "Student":
                Student s = em.find(Student.class, r.getId());
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
        return "Course added";
    }

    @Override
    public Course addCourse(String json) {
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
    public String getRole(long personId, String roleName) throws InvalidRole, NotFoundException {
        if (isValidRole(roleName) == false) {
            throw new InvalidRole(roleName + " is invalid role");
        }
        Person person = em.find(Person.class, personId);
        if (person == null) {
            throw new NotFoundException("No person with that id");
        }
        RoleSchool r = person.getRole(roleName);
        if (r == null) {
            throw new InvalidRole("Person doesn't have that role");
        }
        String response;
        if (roleName.equals("Student")) {
            response = gson.toJson(new StudentDTO((Student) r));
        } else if (roleName.equals("Teacher")) {
            response = gson.toJson(new TeacherDTO((Teacher) r));
        } else {
            response = gson.toJson(new AssistantTeacherDTO((AssistantTeacher) r));
        }
        System.out.println("-----------------response " + response);
        return response;
    }

    @Override
    public String getRoles(long personId) throws NotFoundException {
        Person p = em.find(Person.class, personId);
        if (p == null) {
            throw new NotFoundException("No person with that id");
        }
        Map<String, RoleSchool> m = p.getRoles();
        List<RoleSchoolDTO> roles = new ArrayList();
        for (Map.Entry<String, RoleSchool> entrySet : m.entrySet()) {
            String key = entrySet.getKey();
            RoleSchool value = entrySet.getValue();
            if (key.equals("Student")) {
                roles.add(new StudentDTO((Student) value));
            } else if (key.equals("Teacher")) {
                roles.add(new TeacherDTO((Teacher) value));
            } else {
                roles.add(new AssistantTeacherDTO((AssistantTeacher) value));
            }
        }
        return gson.toJson(roles);
    }

    private boolean isValidRole(String roleName) {
        return roleName.equals("Student") || roleName.equals("Teacher")
                || roleName.equals("AssistantTeacher");
    }

    @Override
    public String getStudentsInCourse(long courseId) throws NotFoundException {
        Course c = em.find(Course.class, courseId);
        if (c == null) {
            throw new NotFoundException("No course with that id");
        }
        List<Student> roles = c.getStudents();
        List<Person> people = em.createNamedQuery("Person.findAll",
                Person.class).getResultList();
        List<PersonDTO> students = new ArrayList();
        for (Person p : people) {
            Student s = (Student) p.getRole("Student");
            if (s != null && roles.contains(s)) {
                students.add(new PersonDTO(p));
            }
        }
        return gson.toJson(students);
    }

    @Override
    public String getAssistantTeachersInCourse(long courseId) throws NotFoundException {
        Course c = em.find(Course.class, courseId);

        if (c == null) {
            throw new NotFoundException("No course with that id");
        }
        List<AssistantTeacher> roles = c.getAssistents();
        List<Person> people = em.createNamedQuery("Person.findAll",
                Person.class).getResultList();
        List<PersonDTO> assTeachers = new ArrayList();
        for (Person p : people) {
            AssistantTeacher at = (AssistantTeacher) p.getRole("AssistantTeacher");
            if (at != null && roles.contains(at)) {
                assTeachers.add(new PersonDTO(p));
            }
        }
        return gson.toJson(assTeachers);
    }

    @Override
    public String getTeachersInCourse(long courseId) throws NotFoundException {
        Course c = em.find(Course.class, courseId);
        if (c == null) {
            throw new NotFoundException("No course with that id");
        }
        List<AssistantTeacher> roles = c.getAssistents();
        List<Person> people = em.createNamedQuery("Person.findAll",
                Person.class).getResultList();
        List<PersonDTO> assTeachers = new ArrayList();
        for (Person p : people) {
            AssistantTeacher at = (AssistantTeacher) p.getRole("AssistantTeacher");
            if (at != null && roles.contains(at)) {
                assTeachers.add(new PersonDTO(p));
            }
        }
        return gson.toJson(assTeachers);
    }

    @Override
    public String getCourses(long roleId) throws InvalidRole, NotFoundException {
        RoleSchool r = em.find(RoleSchool.class, roleId);
        String roleName = r.getRoleName();
        if (!isValidRole(roleName)) {
            throw new InvalidRole("No such role");
        }
        List<CourseDTO> dtoCourses = new ArrayList();
        List<Course> courses;

        if (r == null) {
            throw new NotFoundException("No Such Role");
        }
        if (null != roleName) {
            switch (roleName) {
                case "Student":
                    Student s = em.find(Student.class, roleId);
                    courses = s.getEnrolled();
                    for (Course course : courses) {
                        dtoCourses.add(new CourseDTO(course));
                    }
                    break;
                case "Teacher":
                    Teacher t = em.find(Teacher.class, roleId);
                    courses = t.getTeaches();
                    for (Course course : courses) {
                        dtoCourses.add(new CourseDTO(course));
                    }
                    break;
                default:
                    AssistantTeacher at = em.find(AssistantTeacher.class, roleId);
                    courses = at.getAssists();
                    for (Course course : courses) {
                        dtoCourses.add(new CourseDTO(course));
                    }
                    break;
            }
        }
        return gson.toJson(dtoCourses);
    }
}
