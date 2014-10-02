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

    RoleSchool addRoleToPerson(String json, long id) throws NotFoundException,
            InvalidRole;

    Person deletePerson(long id) throws NotFoundException;

    Course addCourse(String json, long personID, long roleSchoolID, String roleName) throws NotFoundException, InvalidCourseException;

    String getCourses();
}
