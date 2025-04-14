package il.ac.afeka.cloud;

import il.ac.afeka.cloud.model.EmployeeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface EmployeeCRUD extends Neo4jRepository<EmployeeEntity, String> {

    Optional<EmployeeEntity> findByEmailAndPassword(String email, String password);

}