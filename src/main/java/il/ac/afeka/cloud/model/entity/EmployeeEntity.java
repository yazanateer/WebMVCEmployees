package il.ac.afeka.cloud.model.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("Employee")
public class EmployeeEntity {

    @Id
    private String email;

    private String name;
    private String password;

    @Relationship(type = "BIRTHDATE", direction = Relationship.Direction.INCOMING)
    private BirthdateEntity birthdate;

    private List<String> roles;

    //for the bonus
    @Relationship(type = "MANAGED_BY", direction = Relationship.Direction.OUTGOING)
    private EmployeeEntity manager;

    //

    public EmployeeEntity() {
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

    public BirthdateEntity getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(BirthdateEntity birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public EmployeeEntity getManager() {
        return manager;
    }

    public void setManager(EmployeeEntity manager) {
        this.manager = manager;
    }
}