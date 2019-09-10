package nl.neurone;

import nl.neurone.model.PerformanceMeasure;
import nl.neurone.model.TestRun;
//import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class PerformanceMonitorRule implements TestRule {

    private PerformanceLoggerContext performanceLoggerContext = PerformanceLoggerContext.getInstance();
    private boolean performanceExceptionThrown;

    void setPerformanceLoggerContext(PerformanceLoggerContext performanceLoggerContext) {
        this.performanceLoggerContext = performanceLoggerContext;
    }

    @Deprecated // TODO: remove when not used in the end (still an attempt to get testing of Rule correct in TestClassToTest test class
    public boolean isPerformanceExceptionThrown() {
        return performanceExceptionThrown;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String testMethodName = description.getDisplayName();
                TestRun currentRun = performanceLoggerContext.getCurrentRun();
                double averageDuration = performanceLoggerContext.getAverageDurationForMethod(testMethodName);
                long start = System.currentTimeMillis();
                long duration;
                long allowablePerformanceRegression = 0;
                try {
                    base.evaluate();

                    // do performance checking and monitoring
                    duration = System.currentTimeMillis() - start;
                    // Store statistics for future comparison
                    PerformanceMeasure performanceMeasure = new PerformanceMeasure();
                    performanceMeasure.setTestMethodName(testMethodName);
                    performanceMeasure.setDuration(duration);
                    performanceMeasure.setTestRun(currentRun);
                    currentRun.getPerformanceMeasures().add(performanceMeasure);

                    long maxDuration = getMaxDuration(description);
                    if (maxDuration > 0) {
                        System.out.println("Ja hoor, daar is ie dan ;-) --> " + maxDuration);
                        if (duration > maxDuration) {
                            System.out.println("Dat duurt te lang, log het en geeft door als Failure?");
                        }
                    }
                    // If performance regression too great, fail the test!
                    if (duration > 0.0) { // no statistics found, skip comparison!
                        allowablePerformanceRegression = (long) (1.2 * averageDuration);
                        System.out.println("averageDuration: " + averageDuration);
                        System.out.println("allowablePerformanceRegression: " + allowablePerformanceRegression);
                        System.out.println("Duration: " + duration);
                    }
                    performanceExceptionThrown = duration > allowablePerformanceRegression;
                    if (performanceExceptionThrown) { // TODO enable this at a later time when this is necessary, focus on MaxDuration annotation for now
                        System.out.println("throw because slower");
                        throw new PerformanceRegressionException("Method was 20% slower than previous runs");  // TODO: make the percentage configurable
                    }
                } catch (Exception e) {
                    System.out.println("raar hoor....");
                    throw new PerformanceRegressionException(e);
                }
            }

            private long getMaxDuration(Description description) {
                MaxDuration maxDuration = description.getAnnotation(MaxDuration.class);
                return maxDuration == null ? 0L : maxDuration.millis();
            }
        };
    }
}
