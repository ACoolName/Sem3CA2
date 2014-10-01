package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class BusinessAcademy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany
    private List<Person> staffStudents = new ArrayList();
    
    @OneToMany
    private List<Course> courses = new ArrayList();
    
    @OneToMany
    private List<ClassRoom> classes = new ArrayList();
    
    private String name;

    public BusinessAcademy(String name) {
        this.name = name;
    }

    
    public BusinessAcademy() {
    }
    
    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "entity.BusinessAcademy[ id=" + id + " ]";
    }
    
}
