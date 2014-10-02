package dto;

import entity.Course;

public class CourseDTO {

    private Long id;
    private String name;
    private String description;

    public CourseDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public CourseDTO(Course c) {
        this.id = c.getId();
        this.name = c.getName();
        this.description = c.getDescription();
    }
    
    
}
