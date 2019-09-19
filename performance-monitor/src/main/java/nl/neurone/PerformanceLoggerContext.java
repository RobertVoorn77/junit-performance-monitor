package nl.neurone;

import nl.neurone.model.TestRun;
import nl.neurone.repositories.TestRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;

@EnableAutoConfiguration
@Configuration
class PerformanceLoggerContext {
    private static PerformanceLoggerContext instance = null;
    private static ClassPathXmlApplicationContext context = null;
    private TestRun currentRun;
    private Properties props = new Properties();

    private static TestRunRepository testRunRepository;

    @SuppressWarnings("WeakerAccess")
    public PerformanceLoggerContext() {
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
            // start up the spring framework and generate the CrudRepository implementations
            context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
        }
        return instance;
    }

    @Autowired
    public void setTestRunRepository(TestRunRepository testRunRepository) {
        PerformanceLoggerContext.testRunRepository = testRunRepository;
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
        return 0.0; // TODO: build this!! testRunRepository.getAverageDurationForMethod(testMethodName);
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
