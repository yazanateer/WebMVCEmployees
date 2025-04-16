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

//    @GetMapping
//    public List<EmployeeBoundary> getAllEmployees(
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "10") int size){
//        Pageable pageable = Pageable.ofSize(size).withPage(page);
//        return employeeService.getAllEmployees(page, size);
//    }

    @GetMapping
    public ResponseEntity<List<EmployeeBoundary>> getEmployeesByEmailDomain(
            @RequestParam(required = false) String criteria,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if ("byEmailDomain".equals(criteria) && value != null) {
            return ResponseEntity.ok(employeeService.getEmployeesByEmailDomain(value, page, size));
        }
        return ResponseEntity.ok(employeeService.getAllEmployees(page, size));

    }



}
