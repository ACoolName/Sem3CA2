package dto;

public class StudentDTO extends RoleSchoolDTO {
    
    private String semester;

    public StudentDTO(Long id, String roleName, String semester) {
        super(id, roleName);
        this.semester = semester;
    }

}
