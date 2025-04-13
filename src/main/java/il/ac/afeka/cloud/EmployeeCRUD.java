package il.ac.afeka.cloud;

import il.ac.afeka.cloud.model.EmployeeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface EmployeeCRUD extends Neo4jRepository<EmployeeEntity, String> {

}