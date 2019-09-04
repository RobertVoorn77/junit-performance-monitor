package nl.neurone.repositories.map;

import nl.neurone.model.PerformanceMeasure;
import nl.neurone.model.TestRun;
import nl.neurone.repositories.TestRunRepository;

import java.util.*;

public class TestRunRepositoryMapImpl implements TestRunRepository<TestRun> {
    private final Map<Long, TestRun> testRunMap = new HashMap<>();

    {
        PerformanceMeasure performanceMeasure1 = new PerformanceMeasure();
        performanceMeasure1.setId(0L);
        performanceMeasure1.setDuration(10L);
        performanceMeasure1.setTestMethodName("testFastMethod(nl.neurone.TestClassToTest)");
        PerformanceMeasure performanceMeasure2 = new PerformanceMeasure();
        performanceMeasure1.setId(1L);
        performanceMeasure2.setDuration(1000L);
        performanceMeasure2.setTestMethodName("testSlowMethod(nl.neurone.TestClassToTest)");
        TestRun run1 = new TestRun();
        run1.setId(0L);
        run1.getPerformanceMeasures().add(performanceMeasure1);
        run1.getPerformanceMeasures().add(performanceMeasure2);
        testRunMap.put(0L, run1);
        System.out.println(testRunMap);
    }

    @Override
    public void save(TestRun testRun) {
        if (testRun.getId() == null) {
            testRun.setId(getNewId());
        }
        testRunMap.put(testRun.getId(), testRun);
    }

    @Override
    public double getAverageDurationForMethod(String testMethodName) {
        List<Long> durations = new ArrayList<>();
        for (TestRun run : testRunMap.values()) {
            for (PerformanceMeasure measure : run.getPerformanceMeasures()) {
                if (measure.getTestMethodName().equals(testMethodName)) {
                    durations.add(measure.getDuration());
                }
            }
        }

        double average = durations.stream().mapToLong(val -> val).average().orElse(0);
        return average;
    }

    private Long getNewId() {
        return testRunMap.keySet().stream().max(Long::compareTo).map(l -> l + 1).orElse(0L);
    }
}
