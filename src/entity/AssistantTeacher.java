package entity;

import java.util.ArrayList;
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
    private List<Course> courses = new ArrayList();

    public AssistantTeacher() {
        super("AssitantTeacher");
    }

    public void addCourse(Course c) {
        courses.add(c);
    }

    @Override
    public String toString() {
        return "AssisantTeacher{" + "id = " + getId() + ", roleName = "
                + getRoleName() + '}';
    }
}
