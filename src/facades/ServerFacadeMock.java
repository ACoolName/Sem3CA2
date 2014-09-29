package facades;

import exceptions.NotFoundException;
import com.google.gson.Gson;
import entity.Person;
import entity.RoleSchool;
import interfaces.ServerFacade;
import java.util.HashMap;
import java.util.Map;

public class ServerFacadeMock implements ServerFacade {

    Map<Long, Person> persons = new HashMap();
    private Long nextId;
    private final Gson gson = new Gson();

    public ServerFacadeMock() {
        nextId = 0L;
    }

    @Override
    public String getPersons() {
        if (persons.isEmpty()) {
            return null;
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
    public RoleSchool addRoleToPerson(String json, long id) throws NotFoundException {
        RoleSchool r = gson.fromJson(json, RoleSchool.class);
        if(!persons.containsKey(id))
            throw new NotFoundException("No person with that id");
        Person p = persons.get(id);
        p.addRole(r);
        return r;
    }

    @Override
    public Person deletePerson(long id) throws NotFoundException {
        Person p = persons.remove(id);
        if (p == null) {
            throw new NotFoundException("No person exists for the given id");
        }
        return p;
    }

}
