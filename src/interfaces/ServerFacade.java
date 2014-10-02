package interfaces;

import entity.Course;
import entity.Person;
import entity.RoleSchool;
import exceptions.InvalidCourseException;
import exceptions.NotFoundException;
import exceptions.InvalidRole;
import java.util.List;

public interface ServerFacade {

    String getPersons();

    String getPerson(long id) throws NotFoundException;

    Person addPerson(String json);
    
    String getRole(long personId, String roleName) throws InvalidRole;
    
    String getRoles(long personId);

    RoleSchool addRoleToPerson(String json, long id) throws NotFoundException,
            InvalidRole;

    Person deletePerson(long id) throws NotFoundException;

    Course addCourse(String json) throws NotFoundException, InvalidCourseException;

    String getCourses();

    public Course addPersonToCourse(long courseId, long personId, long roleSchoolId,
            String roleName) throws NotFoundException, InvalidCourseException,
            InvalidRole;
}
