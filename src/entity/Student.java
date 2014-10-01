package entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends RoleSchool  {
    private static final long serialVersionUID = 1L;
    
    private String semester;
    @ManyToMany(mappedBy = "students")
    private List<Course> courses;

    public Student() {
    }
    
    public Student (String semester){
        super("Student");
        this.semester=semester;
    }
    
    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Student{" + "id = " + getId() + ", roleName = " +
                getRoleName() + ", Semester = " + semester + '}';
    }
}
