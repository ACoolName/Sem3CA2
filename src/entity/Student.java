package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends RoleSchool  {
    private static final long serialVersionUID = 1L;
    
    private String semester;
    
    public Student (String semester){
        super.setId(12L);
        super.setRoleName("Student");
        this.semester=semester;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
