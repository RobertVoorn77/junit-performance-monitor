package nl.neurone;

import nl.neurone.model.TestRun;
import nl.neurone.repositories.TestRunRepository;
import nl.neurone.repositories.map.TestRunRepositoryMapImpl;

import java.time.LocalDateTime;

public class PerformanceLoggerSingleton {
    private static PerformanceLoggerSingleton instance = null;
    private TestRun currentRun;
    private TestRunRepository testRunRepository = new TestRunRepositoryMapImpl();

    public synchronized static PerformanceLoggerSingleton getInstance() {
        if (instance == null) {
            instance = new PerformanceLoggerSingleton();
        }
        return instance;
    }

    public TestRun getCurrentRun() {
        if (currentRun == null) {
            currentRun = new TestRun();
            currentRun.setStartOfRun(LocalDateTime.now());
            testRunRepository.save(currentRun);
        }
        return currentRun;
    }

    public double getAverageDurationForMethod(String testMethodName) {
        return testRunRepository.getAverageDurationForMethod(testMethodName);
    }
}
