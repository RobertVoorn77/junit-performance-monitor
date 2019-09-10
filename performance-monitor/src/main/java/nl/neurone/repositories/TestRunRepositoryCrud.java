package nl.neurone.repositories;

import nl.neurone.model.TestRun;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRunRepositoryCrud extends CrudRepository<TestRun, Long> {
}
