package dto;

import entity.Person;

public class PersonDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    public PersonDTO(Person p) {
        this.id = p.getId();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.phone = p.getPhone();
        this.email = p.getEmail();
    }
    
}
