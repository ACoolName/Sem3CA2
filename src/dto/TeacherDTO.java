package dto;

import entity.Teacher;

public class TeacherDTO extends RoleSchoolDTO {

    private String degree;
    
    public TeacherDTO(Teacher t) {
        super(t.getId(), t.getRoleName());
        this.degree = t.getDegree();
    }

}
