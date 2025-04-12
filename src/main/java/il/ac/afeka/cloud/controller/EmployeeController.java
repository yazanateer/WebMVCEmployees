package il.ac.afeka.cloud.controller;

import il.ac.afeka.cloud.model.EmployeeBoundary;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    public EmployeeController() {

    }

    @PostMapping
    public ResponseEntity<EmployeeBoundary> createEmployee(@Valid @RequestBody EmployeeBoundary employeeBoundary) {
        try {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

}
