package entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Teacher extends RoleSchool  {
    private static final long serialVersionUID = 1L;
    
    private String degree;
    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses;

    public Teacher() {
    }

    public Teacher(String degree) {
        super("Teacher");
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
    
    @Override
    public String toString() {
        return "Teacher{" + "id = " + getId() + ", roleName = " +
                getRoleName() + ", Degree = " + degree + '}';
    }
}
