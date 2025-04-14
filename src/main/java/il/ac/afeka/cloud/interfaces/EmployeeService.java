package il.ac.afeka.cloud.interfaces;

import il.ac.afeka.cloud.model.EmployeeBoundary;

public interface EmployeeService {
    EmployeeBoundary create(EmployeeBoundary employeeBoundary);
    EmployeeBoundary getByEmailAndPassword(String email, String password);
}
