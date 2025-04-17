package il.ac.afeka.cloud.controller;

import il.ac.afeka.cloud.EmployeeCRUD;
import il.ac.afeka.cloud.interfaces.EmployeeService;
import il.ac.afeka.cloud.model.Birthdate;
import il.ac.afeka.cloud.model.EmployeeBoundary;
import il.ac.afeka.cloud.model.EmployeeEntity;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public List<EmployeeBoundary> getAllEmployees(int page, int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<EmployeeEntity> pageResult = employeeCRUD.findAll(pageable);
        return pageResult.stream().map(entity -> {
            EmployeeBoundary boundary = new EmployeeBoundary(entity);
            boundary.setPassword(null);
            return boundary;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeeBoundary> getEmployeesByEmailDomain(String emailDomain, int page, int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<EmployeeEntity> pageResult = employeeCRUD.findByEmailEndingWith("@" + emailDomain, pageable);
        return pageResult.stream().map(entity -> {
            EmployeeBoundary boundary = new EmployeeBoundary(entity);
            boundary.setPassword(null);
            return boundary;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeeBoundary> getEmployeesByRole(String role, int page, int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<EmployeeEntity> pageResult = employeeCRUD.findByRolesContaining(role, pageable);
        return pageResult.stream().map(entity -> {
            EmployeeBoundary boundary = new EmployeeBoundary(entity);
            boundary.setPassword(null);
            return boundary;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeeBoundary> getEmployeesByAge(int age, int page, int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusYears(age + 1);
        LocalDate to = today.minusYears(age);

        return employeeCRUD.findAll(pageable).stream().filter(e -> {
            Birthdate birthdate = e.getBirthdate();
            if(birthdate == null)
                return false;
            try{
                LocalDate localBirthdate = LocalDate.of(
                        Integer.parseInt(birthdate.getYear()),
                        Integer.parseInt(birthdate.getMonth()),
                        Integer.parseInt(birthdate.getDay())
                );
                return localBirthdate.isAfter(from) && localBirthdate.isBefore(to);
            } catch(Exception ex){
                return false;
            }
        }).map(EmployeeBoundary::new)
                .peek(boundary -> boundary.setPassword(null)) // hide password
                .collect(Collectors.toList());

    }
    @Override
    @Transactional
    public void deleteAll() {
        employeeCRUD.deleteAllWithRelations();

    }

    @Override
    @Transactional
    public void assignManager(String employeeEmail, String managerEmail) {
        EmployeeEntity employee = employeeCRUD.findById(employeeEmail).orElseThrow(() -> new RuntimeException("Employee not found"));
        EmployeeEntity manager = employeeCRUD.findById(managerEmail).orElseThrow(() -> new RuntimeException("Employee not found"));
        System.out.println("🔥 Called assignManager implementaion for: "  + employee.getEmail() + " : " + manager.getEmail() );


        if (employee.getManager() != null && employee.getManager().getEmail().equals(managerEmail)) return ;
        System.out.println("🔥 passed the if statement : " );

        employee.setManager(manager);
        employeeCRUD.save(employee);
    }

}
