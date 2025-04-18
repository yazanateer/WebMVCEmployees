package il.ac.afeka.cloud.controller;

import il.ac.afeka.cloud.EmployeeCRUD;
import il.ac.afeka.cloud.interfaces.EmployeeService;
import il.ac.afeka.cloud.model.EmployeeBoundary;
import il.ac.afeka.cloud.model.EmployeeEntity;
import il.ac.afeka.cloud.model.ManagerEmailBoundary;
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
    public ResponseEntity<Void> assignManager(
            @PathVariable String employeeEmail,
            @RequestBody ManagerEmailBoundary managerBoundary
    ){
        System.out.println("🔥 Called assignManager for: " + employeeEmail + " : " + managerBoundary.getEmail());

        try {
            employeeService.assignManager(employeeEmail, managerBoundary.getEmail());
            return ResponseEntity.ok().build();
        } catch(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @GetMapping("/{employeeEmail}/manager")
    public ResponseEntity<EmployeeBoundary> getManagerOfEmployee(@PathVariable String employeeEmail) {
        try {
            EmployeeBoundary manager = employeeService.getManagerOfEmployee(employeeEmail);
            return ResponseEntity.ok(manager);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
    public ResponseEntity<Void> removeManager(@PathVariable String employeeEmail) {
        try{
            employeeService.removeManager(employeeEmail);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}