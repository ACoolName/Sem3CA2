package facades;

import exceptions.NotFoundException;
import com.google.gson.Gson;
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
    private Long nextId;
    private Long nextRoleId;
    private final Gson gson = new Gson();

    public ServerFacadeMock() {
        nextId = 0L;
        nextRoleId = 0L;
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
    public Course addCourse(String json) throws NotFoundException, InvalidCourseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCourses() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Course addPersonToCourse(long courseId, long personId, long roleSchoolId, String roleName) throws NotFoundException, InvalidCourseException, InvalidRole {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRole(long personId, String roleName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRoles(long personId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
