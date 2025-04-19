package il.ac.afeka.cloud.service;

import il.ac.afeka.cloud.CRUD.BirthdateCRUD;
import il.ac.afeka.cloud.CRUD.EmployeeCRUD;
import il.ac.afeka.cloud.exception.EmployeeNotFoundException;
import il.ac.afeka.cloud.model.entity.BirthdateEntity;
import il.ac.afeka.cloud.model.boundary.EmployeeBoundary;
import il.ac.afeka.cloud.model.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeImplementation implements EmployeeService {

    private final EmployeeCRUD employeeCRUD;
    private final BirthdateCRUD birthdateCRUD;


    public EmployeeImplementation(EmployeeCRUD employeeCRUD, BirthdateCRUD birthdateCRUD) {
        this.employeeCRUD = employeeCRUD;
        this.birthdateCRUD = birthdateCRUD;
    }

    @Override
    @Transactional
    public EmployeeBoundary create(EmployeeBoundary employeeBoundary) {
        String email = employeeBoundary.getEmail();

        if (employeeCRUD.findById(email).isPresent()) {
            throw new RuntimeException("Employee with email already exists: " + email);
        }

        EmployeeEntity entity = employeeBoundary.toEntity();

        String day = employeeBoundary.getBirthdate().getDay();
        String month = employeeBoundary.getBirthdate().getMonth();
        String year = employeeBoundary.getBirthdate().getYear();
        String birthdateId = day + "-" + month + "-" + year;

        Optional<BirthdateEntity> existingBirthdate = birthdateCRUD.findById(birthdateId);
        if (existingBirthdate.isPresent()) {
            entity.setBirthdate(existingBirthdate.get());
        } else {
            BirthdateEntity newBirthdate = new BirthdateEntity(day, month, year);
            birthdateCRUD.save(newBirthdate);
            entity.setBirthdate(newBirthdate);
        }

        EmployeeEntity savedEntity = employeeCRUD.save(entity);
        EmployeeBoundary response = new EmployeeBoundary(savedEntity);
        response.setPassword(null);
        return response;
    }



    @Override
    @Transactional(readOnly = true)
    public EmployeeBoundary getByEmailAndPassword(String email, String password) {
        EmployeeEntity entity = employeeCRUD.findByEmailAndPassword(email, password).orElseThrow(() -> new EmployeeNotFoundException("Employee with given credentials not found"));
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
                    BirthdateEntity birthdate = e.getBirthdate();
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
    EmployeeEntity employee = employeeCRUD.findById(employeeEmail)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    EmployeeEntity manager = employeeCRUD.findById(managerEmail)
            .orElseThrow(() -> new EmployeeNotFoundException("Manager not found"));

    BirthdateEntity existingBirthdate = employee.getBirthdate();
    employee.setBirthdate(existingBirthdate);

    if (employee.getManager() != null && employee.getManager().getEmail().equals(managerEmail)) return;

    else if (employee.getManager() != null && !employee.getManager().getEmail().equals(managerEmail)) {
        removeManager(employeeEmail);
    }

    employee.setManager(manager);
    employeeCRUD.save(employee);
}

    @Override
    @Transactional(readOnly = true)
    public EmployeeBoundary getManagerOfEmployee(String employeeEmail) {
        EmployeeEntity employee = employeeCRUD.findById(employeeEmail)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        EmployeeEntity manager = employee.getManager();
        if (manager == null) {
            throw new EmployeeNotFoundException("Manager not assigned");
        }

        EmployeeBoundary boundary = new EmployeeBoundary(manager);
        boundary.setPassword(null);
        return boundary;
    }


    @Override
    @Transactional(readOnly = true)
    public List<EmployeeBoundary> getSubordinates(String managerEmail, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<EmployeeEntity> result = employeeCRUD.findSubordinatesByManagerEmail(managerEmail, pageable);
        return result.stream()
                .map(entity -> {
                    System.out.println(entity);
                    EmployeeBoundary boundary = new EmployeeBoundary(entity);
                    System.out.println(boundary);
                    boundary.setPassword(null);
                    return boundary;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeManager(String employeeEmail) {
        if (!employeeCRUD.existsById(employeeEmail)) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        employeeCRUD.removeManagerRelation(employeeEmail);
    }
}