package entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends RoleSchool  {
    private static final long serialVersionUID = 1L;
    
    private String semester;

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
