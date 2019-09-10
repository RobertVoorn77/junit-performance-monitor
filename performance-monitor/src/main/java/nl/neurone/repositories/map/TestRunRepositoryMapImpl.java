package nl.neurone.repositories.map;

import nl.neurone.model.PerformanceMeasure;
import nl.neurone.model.TestRun;
import nl.neurone.repositories.TestRunRepository;

import java.util.*;

public class TestRunRepositoryMapImpl implements TestRunRepository<TestRun> {
    private final Map<Long, TestRun> testRunMap = new HashMap<>();

    {
        createTestRun(0L,
                createPerformanceMeasure(0L, 10L, "testFastMethodThrowsException(nl.neurone.TestClassToTest)"),
                createPerformanceMeasure(1L,100L,"testSlowMethodThrowsException(nl.neurone.TestClassToTest)"));
        createTestRun(1L,
                createPerformanceMeasure(0L, 50L, "testFastMethod(nl.neurone.TestClassToTest)"),
                createPerformanceMeasure(1L,5000L,"testSlowMethod(nl.neurone.TestClassToTest)"));
        System.out.println(testRunMap);
    }

    private void createTestRun(long id, PerformanceMeasure... performanceMeasures) {
        TestRun run = new TestRun();
        run.setId(id);
        for (PerformanceMeasure performanceMeasure : performanceMeasures) {
            run.getPerformanceMeasures().add(performanceMeasure);
        }
        testRunMap.put(id, run);
    }

    private PerformanceMeasure createPerformanceMeasure(long id, long duration, String testMethodName) {
        PerformanceMeasure performanceMeasure1 = new PerformanceMeasure();
        performanceMeasure1.setDuration(duration);
        performanceMeasure1.setId(id);
        performanceMeasure1.setTestMethodName(testMethodName);
        return performanceMeasure1;
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

        return durations.stream().mapToLong(val -> val).average().orElse(0);
    }

    private Long getNewId() {
        return testRunMap.keySet().stream().max(Long::compareTo).map(l -> l + 1).orElse(0L);
    }
}
