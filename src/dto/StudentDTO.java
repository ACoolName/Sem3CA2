package dto;

import entity.Student;

public class StudentDTO extends RoleSchoolDTO {
    
    private String semester;

    public StudentDTO(Student s) {
        super(s.getId(), s.getRoleName());
        this.semester = s.getSemester();
    }

}
