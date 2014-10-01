package entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class AssistantTeacher extends RoleSchool {

    private static final long serialVersionUID = 1L;
    @ManyToMany(mappedBy = "assistantTeachers")
    private List<Course> courses;

    public AssistantTeacher() {
        super("AssitantTeacher");
    }

    @Override
    public String toString() {
        return "AssisantTeacher{" + "id = " + getId() + ", roleName = "
                + getRoleName() + '}';
    }
}
