package nl.neurone.repositories;

import nl.neurone.model.TestRun;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRunRepository extends CrudRepository<TestRun, Long> {
//    double getAverageDurationForMethod(String testMethodName);    TODO: Implement this and then use it in PerformanceLoggerContext class
}
