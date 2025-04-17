package il.ac.afeka.cloud.model;

public class ManagerEmailBoundary {
    private String email;

    public ManagerEmailBoundary() {

    }

    public ManagerEmailBoundary(String email) {
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
