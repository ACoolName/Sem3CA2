package dto;

import entity.AssistantTeacher;

public class AssistantTeacherDTO extends RoleSchoolDTO {

    public AssistantTeacherDTO(AssistantTeacher at) {
        super(at.getId(), at.getRoleName());
    }
}
