package il.ac.afeka.cloud.interfaces;

import il.ac.afeka.cloud.model.EmployeeBoundary;

import java.util.List;

public interface EmployeeService {
    EmployeeBoundary create(EmployeeBoundary employeeBoundary);
    EmployeeBoundary getByEmailAndPassword(String email, String password);
    List<EmployeeBoundary> getAllEmployees(int page, int size);
    List<EmployeeBoundary> getEmployeesByEmailDomain(String emailDomain, int page, int size);
}
