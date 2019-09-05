package nl.neurone;

import nl.neurone.model.TestRun;
import nl.neurone.repositories.TestRunRepository;
import nl.neurone.repositories.map.TestRunRepositoryMapImpl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;

class PerformanceLoggerContext {
    private static PerformanceLoggerContext instance = null;
    private TestRun currentRun;
    private TestRunRepository<TestRun> testRunRepository = new TestRunRepositoryMapImpl();
    private Properties props = new Properties();

    private PerformanceLoggerContext() {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/performanceMonitor.properties");
            props.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized static PerformanceLoggerContext getInstance() {
        if (instance == null) {
            instance = new PerformanceLoggerContext();
        }
        return instance;
    }

    TestRun getCurrentRun() {
        if (currentRun == null) {
            currentRun = new TestRun();
            currentRun.setStartOfRun(LocalDateTime.now());
            testRunRepository.save(currentRun);
        }
        return currentRun;
    }

    double getAverageDurationForMethod(String testMethodName) {
        return testRunRepository.getAverageDurationForMethod(testMethodName);
    }

    String getDriver() {
        return props.getProperty("persistence.driver");
    }

    String getUrl() {
        return props.getProperty("persistence.jdbcUrl");
    }

    String getUser() {
        return props.getProperty("persistence.user");
    }

    String getPassword() {
        return props.getProperty("persistence.password");
    }
}
