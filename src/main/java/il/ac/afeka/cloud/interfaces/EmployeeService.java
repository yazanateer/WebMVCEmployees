package il.ac.afeka.cloud.interfaces;

import il.ac.afeka.cloud.model.EmployeeBoundary;
import il.ac.afeka.cloud.model.ManagerEmailBoundary;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface EmployeeService {
    EmployeeBoundary create(EmployeeBoundary employeeBoundary);
    EmployeeBoundary getByEmailAndPassword(String email, String password);
    List<EmployeeBoundary> getAllEmployees(int page, int size);
    List<EmployeeBoundary> getEmployeesByEmailDomain(String emailDomain, int page, int size);
    List<EmployeeBoundary> getEmployeesByRole(String role, int page, int size);
    List<EmployeeBoundary> getEmployeesByAge(int age, int page, int size);
    void deleteAll();
    void assignManager(String employeeEmail, String managerEmail);
    EmployeeBoundary getManagerOfEmployee(String employeeEmail);
    List<EmployeeBoundary> getSubordinates(String managerEmail, int page, int size);
    void removeManager(String employeeEmail);

}
