package nl.neurone;

import nl.neurone.model.TestRun;
import nl.neurone.repositories.TestRunRepository;
import nl.neurone.repositories.TestRunRepositoryCrud;
import nl.neurone.repositories.map.TestRunRepositoryMapImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@EnableAutoConfiguration
@Configuration
class PerformanceLoggerContext {
    private static PerformanceLoggerContext instance = null;
    private TestRun currentRun;
    private TestRunRepository<TestRun> testRunRepository = new TestRunRepositoryMapImpl();
    private Properties props = new Properties();

    private static TestRunRepositoryCrud testRunRepositoryCrud;

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
        }
        return instance;
    }

    @Autowired
    public void setTestRunRepositoryCrud(TestRunRepositoryCrud testRunRepositoryCrud) {
        PerformanceLoggerContext.testRunRepositoryCrud = testRunRepositoryCrud;
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

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});

        System.err.println("--------------------------------");
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.err.println("--------------------------------");
        System.out.println("testRunRepositoryCrud: " + testRunRepositoryCrud);
        TestRun t1 = new TestRun();
        t1.setStartOfRun(LocalDateTime.now());
        testRunRepositoryCrud.save(t1);
        List<TestRun> result = (List<TestRun>) testRunRepositoryCrud.findAll();
        System.out.println("result: "+ result);
    }
}
