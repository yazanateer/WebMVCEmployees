package il.ac.afeka.cloud;

import il.ac.afeka.cloud.model.Birthdate;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface BirthdateCRUD extends Neo4jRepository<Birthdate, String> {
}
