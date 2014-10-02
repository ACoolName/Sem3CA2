package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50, nullable = false)
    private String firstName;
    
    @Column(length = 50, nullable = false)
    private String lastName;
    
    @Column(length = 50, nullable = false)
    private String phone;
    
    @Column(length = 50, nullable = false)
    private String email;

    @OneToMany
    private Map<String, RoleSchool> roles;
    
    public RoleSchool getRole(String roleName) {
        return roles.containsKey(roleName) ? roles.get(roleName) : null;
    }

    public Map<String, RoleSchool> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, RoleSchool> roles) {
        this.roles = roles;
    }

    public void addRole(RoleSchool role) {
        roles.put(role.getRoleName(), role);
    }

    public Person() {
        roles = new HashMap<>();
    }

    public Person(String firstName, String lastName, String phone, String email) {
        roles = new HashMap<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person{" + "id = " + id + ", firstName = " + firstName + ", lastName = " + lastName + ", phone = " + phone + ", email = " + email + ", roles = " + roles + '}';
    }

}
