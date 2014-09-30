package entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class AssistantTeacher extends RoleSchool {

    private static final long serialVersionUID = 1L;

    public AssistantTeacher() {
        super("AssitantTeacher");
    }

    @Override
    public String toString() {
        return "AssisantTeacher{" + "id = " + getId() + ", roleName = "
                + getRoleName() + '}';
    }
}
