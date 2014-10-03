package entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(name = "Teacher.findAll", query = "SELECT t FROM Teacher t")
})
public class Teacher extends RoleSchool {

    private static final long serialVersionUID = 1L;

    @Column(length = 150, nullable = false)
    private String degree;
    
    @ManyToMany(mappedBy = "taughtBy")
    private List<Course> teaches = new ArrayList();
    
    public Teacher() {
    }

    public List<Course> getTeaches() {
        return teaches;
    }

    public void addCourse(Course c) {
        teaches.add(c);
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
        return "Teacher{" + "id = " + getId() + ", roleName = "
                + getRoleName() + ", Degree = " + degree + '}';
    }
}
