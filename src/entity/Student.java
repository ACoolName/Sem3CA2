package entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s")
})
public class Student extends RoleSchool  {
    private static final long serialVersionUID = 1L;
    
    @Column(length = 50, nullable = false)
    private String semester;
    
    @ManyToMany(mappedBy = "students")
    private List<Course> courses = new ArrayList();

    public Student() {
    }
    
    public void addCourse(Course c) {
        courses.add(c);
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
