package il.ac.afeka.cloud.model;


import jakarta.validation.constraints.*;

import java.util.List;

public class EmployeeBoundary {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @Size(min = 8, message = "Password must have at least 3 characters")
    private String password;

    @NotNull
    private BirthdateBoundary birthdate;

    @NotEmpty
    private List<@NotBlank String> roles;


    public EmployeeBoundary() { //empty constructor

    }

    public EmployeeBoundary(EmployeeEntity entity) {
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.password = entity.getPassword();
        this.birthdate = new BirthdateBoundary(entity.getBirthdate());
        this.roles = entity.getRoles();
    }

    public EmployeeEntity toEntity() {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmail(this.email);
        entity.setName(this.name);
        entity.setPassword(this.password);
        entity.setBirthdate(this.birthdate.toEntity());
        entity.setRoles(this.roles);
        return entity;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public BirthdateBoundary getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(BirthdateBoundary birthdate) {
        this.birthdate = birthdate;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
