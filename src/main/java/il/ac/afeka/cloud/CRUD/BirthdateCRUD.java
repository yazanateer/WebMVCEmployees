package il.ac.afeka.cloud.CRUD;

import il.ac.afeka.cloud.model.entity.BirthdateEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface BirthdateCRUD extends Neo4jRepository<BirthdateEntity, String> {
}
