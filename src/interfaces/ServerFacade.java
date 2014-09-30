package interfaces;

import entity.Person;
import entity.RoleSchool;
import exceptions.NotFoundException;

public interface ServerFacade {
    String getPersons();
    String getPerson(long id) throws NotFoundException;
    Person addPerson(String json);
    RoleSchool addRoleToPerson(String json, long id) throws NotFoundException;
    Person deletePerson(long id) throws NotFoundException;
}
