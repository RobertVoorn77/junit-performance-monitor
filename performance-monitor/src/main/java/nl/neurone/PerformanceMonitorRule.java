package nl.neurone;

import nl.neurone.model.PerformanceMeasure;
import nl.neurone.model.TestRun;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class PerformanceMonitorRule implements TestRule {

    private PerformanceLoggerSingleton performanceLogger = PerformanceLoggerSingleton.getInstance();

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String testMethodName = description.getDisplayName();
                TestRun currentRun = performanceLogger.getCurrentRun();
                double averageDuration = performanceLogger.getAverageDurationForMethod(testMethodName);
                long start = System.currentTimeMillis();
                try {
                    base.evaluate();
                } finally {
                    long duration = System.currentTimeMillis() - start;
                    if (duration > 0.0) { // no statistics found, skip comparison!
                        long tooMuchSlower = (long) (1.2 * averageDuration);
                        if (duration > tooMuchSlower) {
                            System.err.println("This test has gotten too slow! ==> " + testMethodName);
                        }
                    }
                    PerformanceMeasure performanceMeasure = new PerformanceMeasure();
                    performanceMeasure.setTestMethodName(testMethodName);
                    performanceMeasure.setDuration(duration);
                    performanceMeasure.setTestRun(currentRun);
                    currentRun.getPerformanceMeasures().add(performanceMeasure);
                    System.err.println("performanceMeasure: " + performanceMeasure);
                }
            }
        };
    }
}
