package facades;

import exceptions.NotFoundException;
import com.google.gson.Gson;
import dto.CourseDTO;
import entity.AssistantTeacher;
import entity.Course;
import entity.Person;
import entity.RoleSchool;
import entity.Student;
import entity.Teacher;
import exceptions.InvalidCourseException;
import exceptions.InvalidRole;
import interfaces.ServerFacade;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerFacadeMock implements ServerFacade {

    Map<Long, Person> persons = new HashMap();
    Map<Long, Course> courses = new HashMap();
    private Long nextId;
    private Long nextRoleId;
    private Long nextCourseId;
    private final Gson gson = new Gson();

    public ServerFacadeMock() {
        nextId = 0L;
        nextRoleId = 0L;
        nextCourseId = 0L;
    }

    private boolean isValidRole(String roleName) {
        return roleName.equals("Student") || roleName.equals("Teacher")
                || roleName.equals("AssistantTeacher");
    }

    @Override
    public String getPersons() {
        if (persons.isEmpty()) {
            return "[]";
        }
        return gson.toJson(persons.values());
    }

    @Override
    public String getPerson(long id) throws NotFoundException {
        Person p = persons.get(id);
        if (p == null) {
            throw new NotFoundException("No person exists for the given id");
        }
        return gson.toJson(p);
    }

    @Override
    public Person addPerson(String json) {
        Person p = gson.fromJson(json, Person.class);
        p.setId(nextId);
        persons.put(nextId, p);
        nextId++;
        return p;
    }

    @Override
    public RoleSchool addRoleToPerson(String json, long id) throws
            NotFoundException, InvalidRole {
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
        r.setId(nextRoleId++);
        if (!persons.containsKey(id)) {
            throw new NotFoundException("No person with that id");
        }
        Person p = persons.get(id);
        p.addRole(r2);
        return r2;
    }

    @Override
    public Person deletePerson(long id) throws NotFoundException {
        Person p = persons.remove(id);
        if (p == null) {
            throw new NotFoundException("No person exists for the given id");
        }
        return p;
    }

    @Override
    public Course addCourse(String json) {
        Course c = gson.fromJson(json, Course.class);
        c.setId(nextCourseId++);
        courses.put(c.getId(), c);
        return c;
    }

    @Override
    public String getCourses() {
        return gson.toJson(courses.values());
    }

    @Override
    public String addPersonToCourse(long courseId, long personId,
            long roleSchoolId, String roleName) throws NotFoundException,
            InvalidCourseException, InvalidRole {
        if(!isValidRole(roleName)) throw new InvalidRole("No such role");
        if (!persons.containsKey(personId)) {
            throw new NotFoundException("No person with that id");
        }
        if(!courses.containsKey(courseId)) 
            throw new InvalidCourseException("No course with that id");
        Person p = persons.get(personId);
        RoleSchool r = p.getRole(roleName);
        Course c = courses.get(courseId);
        switch (roleName) {
            case "Student":
                Student s = (Student) r;
                c.addStudent(s);
                s.addCourse(c);
                break;
            case "Teacher":
                Teacher t = (Teacher) r;
                c.addTeacher(t);
                t.addCourse(c);
                break;
            default:
                AssistantTeacher at = (AssistantTeacher) r;
                c.addAssistantTeacher(at);
                at.addCourse(c);
                break;
        }
        return "Course added";
    }

    @Override
    public String getRole(long personId, String roleName) {
        return gson.toJson(persons.get(personId).getRole(roleName));
    }

    @Override
    public String getRoles(long personId) {
        return gson.toJson(persons.get(personId).getRoles());
    }

    @Override
    public String getStudentsInCourse(long courseId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAssistantTeachersInCourse(long courseId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTeachersInCourse(long courseId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCourse(long courseId) {
        return gson.toJson(courses.get(courseId));
    }

    @Override
    public String getCourses(long roleId, String roleName) throws InvalidRole, NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
