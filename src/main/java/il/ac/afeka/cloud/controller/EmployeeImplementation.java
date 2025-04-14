package il.ac.afeka.cloud.controller;

import il.ac.afeka.cloud.EmployeeCRUD;
import il.ac.afeka.cloud.interfaces.EmployeeService;
import il.ac.afeka.cloud.model.EmployeeBoundary;
import il.ac.afeka.cloud.model.EmployeeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeImplementation implements EmployeeService {

    private final EmployeeCRUD employeeCRUD;

    public EmployeeImplementation(EmployeeCRUD employeeCRUD) {
        this.employeeCRUD = employeeCRUD;
    }

    @Override
    @Transactional
    public EmployeeBoundary create(EmployeeBoundary employeeBoundary) {
        String email = employeeBoundary.getEmail();
        System.out.println("Trying to create employee with email: " + email);
        if (employeeCRUD.findById(email).isPresent()) {
            throw new RuntimeException("Employee with email already exists: " + email);
        }
        EmployeeEntity entity = employeeBoundary.toEntity();
        EmployeeEntity savedEntity = employeeCRUD.save(entity);
        EmployeeBoundary response = new EmployeeBoundary(savedEntity);
        response.setPassword(null);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeBoundary getByEmailAndPassword(String email, String password) {
        EmployeeEntity entity = employeeCRUD.findByEmailAndPassword(email, password).orElseThrow(() -> new RuntimeException("Employee not found"));
        EmployeeBoundary boundary = new EmployeeBoundary(entity);
        boundary.setPassword(null);
        return boundary;
    }
}
