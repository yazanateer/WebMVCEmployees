package il.ac.afeka.cloud.controller;

import il.ac.afeka.cloud.EmployeeCRUD;
import il.ac.afeka.cloud.interfaces.EmployeeService;
import il.ac.afeka.cloud.model.EmployeeBoundary;
import il.ac.afeka.cloud.model.EmployeeEntity;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeCRUD employeeCRUD;


    public EmployeeController(EmployeeService employeeService, EmployeeCRUD employeeCRUD) {
        this.employeeService = employeeService;
        this.employeeCRUD = employeeCRUD;
    }

    @PostMapping
    public ResponseEntity<EmployeeBoundary> createEmployee(@Valid @RequestBody EmployeeBoundary employeeBoundary) {
        try {
            EmployeeBoundary createdEmployee = employeeService.create(employeeBoundary);
            return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{employeeEmail}")
    public ResponseEntity<EmployeeBoundary> getEmployeeByEmailAndPassword(
            @PathVariable String employeeEmail,
            @RequestParam String password){

        try{
            EmployeeBoundary employee = employeeService.getByEmailAndPassword(employeeEmail, password);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch(RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //in this function you can search employees including pagination, you can choose to use criteria to serach, and you can search the employees without criteria
    @GetMapping
    public ResponseEntity<List<EmployeeBoundary>> searchEmployees(
            @RequestParam(required = false) String criteria,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if ((criteria == null) && (value == null)) {
            return ResponseEntity.ok(employeeService.getAllEmployees(page, size));
        }

        assert criteria != null;
        return switch (criteria) {
            case "byEmailDomain" -> ResponseEntity.ok(employeeService.getEmployeesByEmailDomain(value, page, size));
            case "byRole" -> ResponseEntity.ok(employeeService.getEmployeesByRole(value, page, size));
            case "byAge" -> ResponseEntity.ok(employeeService.getEmployeesByAge(Integer.parseInt(value), page, size));
            default ->  ResponseEntity.ok(List.of());
        };

    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEmployee() {
        employeeService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
