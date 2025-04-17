package il.ac.afeka.cloud;

import il.ac.afeka.cloud.model.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface EmployeeCRUD extends Neo4jRepository<EmployeeEntity, String> {

    Optional<EmployeeEntity> findByEmailAndPassword(String email, String password);
    Page<EmployeeEntity> findByEmailEndingWith(String email, Pageable pageable);
    Page<EmployeeEntity> findByRolesContaining(String role, Pageable pageable);

    @Query("MATCH (n) DETACH DELETE n")
    void deleteAllWithRelations();
}