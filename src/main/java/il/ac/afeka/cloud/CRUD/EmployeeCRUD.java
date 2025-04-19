package il.ac.afeka.cloud.CRUD;

import il.ac.afeka.cloud.model.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeCRUD extends Neo4jRepository<EmployeeEntity, String> {

    Optional<EmployeeEntity> findByEmailAndPassword(String email, String password);
    Page<EmployeeEntity> findByEmailEndingWith(String email, Pageable pageable);
    Page<EmployeeEntity> findByRolesContaining(String role, Pageable pageable);

    @Query("MATCH (n) DETACH DELETE n")
    void deleteAllWithRelations();

    @Query(
        value = """
        MATCH (e:Employee)-[:MANAGED_BY]->(m:Employee {email: $managerEmail})
        OPTIONAL MATCH (e)-[:BIRTHDATE]->(b:Birthdate)
        RETURN e, collect(b)
        """,
        countQuery = """
        MATCH (e:Employee)-[:MANAGED_BY]->(m:Employee {email: $managerEmail})
        RETURN count(e)
        """)

    Page<EmployeeEntity> findSubordinatesByManagerEmail(@Param("managerEmail") String managerEmail, Pageable pageable);

    @Query("MATCH (e:Employee {email: $email})-[r:MANAGED_BY]->() DELETE r")
    void removeManagerRelation(String email);


}