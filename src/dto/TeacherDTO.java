package dto;

public class TeacherDTO extends RoleSchoolDTO {

    private String degree;
    
    public TeacherDTO(Long id, String roleName, String degree) {
        super(id, roleName);
        this.degree = degree;
    }

}
