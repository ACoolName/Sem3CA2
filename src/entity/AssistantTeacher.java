package entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(name = "AssistantTeacher.findAll", query = "SELECT at FROM AssistantTeacher at")
})
public class AssistantTeacher extends RoleSchool {

    private static final long serialVersionUID = 1L;

    @ManyToMany(mappedBy = "assistents")
    private List<Course> assists = new ArrayList();

    public List<Course> getAssists() {
        return assists;
    }

    public AssistantTeacher() {
        super("AssitantTeacher");
    }

    public void addCourse(Course c) {
        assists.add(c);
    }

    @Override
    public String toString() {
        return "AssisantTeacher{" + "id = " + getId() + ", roleName = "
                + getRoleName() + '}';
    }
}
