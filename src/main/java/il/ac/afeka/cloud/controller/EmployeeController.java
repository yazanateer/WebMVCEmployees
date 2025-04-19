package il.ac.afeka.cloud.controller;

import il.ac.afeka.cloud.CRUD.EmployeeCRUD;
import il.ac.afeka.cloud.exception.EmployeeNotFoundException;
import il.ac.afeka.cloud.service.EmployeeService;
import il.ac.afeka.cloud.model.boundary.EmployeeBoundary;
import il.ac.afeka.cloud.model.boundary.ManagerEmailBoundary;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeCRUD employeeCRUD;


    public EmployeeController(EmployeeService employeeService, EmployeeCRUD employeeCRUD) {
        this.employeeService = employeeService;
        this.employeeCRUD = employeeCRUD;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EmployeeBoundary> createEmployee(@Valid @RequestBody EmployeeBoundary employeeBoundary) {
        try {
            EmployeeBoundary createdEmployee = employeeService.create(employeeBoundary);
            return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/{employeeEmail}")
    public ResponseEntity<?> getEmployeeByEmailAndPassword(
            @PathVariable String employeeEmail,
            @RequestParam String password) {

        try {
            EmployeeBoundary employee = employeeService.getByEmailAndPassword(employeeEmail, password);
            return ResponseEntity.ok(employee);
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

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

    @PutMapping("/{employeeEmail}/manager")
    public ResponseEntity<?> assignManager(
            @PathVariable String employeeEmail,
            @RequestBody ManagerEmailBoundary managerBoundary) {

        try {
            employeeService.assignManager(employeeEmail, managerBoundary.getEmail());
            return ResponseEntity.ok().build();
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{employeeEmail}/manager")
    public ResponseEntity<?> getManagerOfEmployee(@PathVariable String employeeEmail) {
        try {
            EmployeeBoundary manager = employeeService.getManagerOfEmployee(employeeEmail);
            return ResponseEntity.ok(manager);
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }


    @GetMapping("/{managerEmail}/subordinates")
    public ResponseEntity<List<EmployeeBoundary>> getSubordinates(
            @PathVariable String managerEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getSubordinates(managerEmail, page, size));
    }

    @DeleteMapping("/{employeeEmail}/manager")
    public ResponseEntity<?> removeManager(@PathVariable String employeeEmail) {
        try {
            employeeService.removeManager(employeeEmail);
            return ResponseEntity.noContent().build();
        } catch (EmployeeNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}