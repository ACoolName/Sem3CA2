package auxiliary;

import entity.RoleSchool;

public class RoleSchoolAndPersonId
{
    private Long personId;
    private RoleSchool role;

    public RoleSchoolAndPersonId(Long personId, RoleSchool role) {
        this.personId = personId;
        this.role = role;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public RoleSchool getRole() {
        return role;
    }

    public void setRole(RoleSchool role) {
        this.role = role;
    }

    
}
