package nl.neurone;

import nl.neurone.model.PerformanceMeasure;
import nl.neurone.model.TestRun;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class PerformanceMonitorRuleTest {

    private static final TestRun testRun = new TestRun();
    private PerformanceLoggerContext context = mock(PerformanceLoggerContext.class);
    private PerformanceMonitorRule rule = new PerformanceMonitorRule();
    private static final String TEST_METHOD_NAME = "TEST_METHOD_NAME";


    @Before
    public void setUp() {
        rule.setPerformanceLoggerContext(context);
        when(context.getCurrentRun()).thenReturn(testRun);
        when(context.getAverageDurationForMethod(TEST_METHOD_NAME)).thenReturn(50.0);
        Description description = mock(Description.class);
        when(description.getDisplayName()).thenReturn(TEST_METHOD_NAME);
        Statement base = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Thread.sleep(100);
            }
        };
        Statement statement = rule.apply(base, description);
    }


    @Test(expected = PerformanceRegressionException.class)
    public void applyTooMuchPerformanceRegressionThrowsException() throws Throwable {
        // given
        PerformanceLoggerContext context = mock(PerformanceLoggerContext.class);
        rule.setPerformanceLoggerContext(context);
        when(context.getCurrentRun()).thenReturn(new TestRun());
        when(context.getAverageDurationForMethod(TEST_METHOD_NAME)).thenReturn(50.0);
        Description description = mock(Description.class);
        when(description.getDisplayName()).thenReturn(TEST_METHOD_NAME);
        Statement base = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Thread.sleep(100);
            }
        };
        Statement statement = rule.apply(base, description);

        // when
        statement.evaluate();
    }

    @Test
    public void applyNoPerformanceRegression() throws Throwable {
        // given
        final String testMethodName = "testMethodName";
        PerformanceLoggerContext context = mock(PerformanceLoggerContext.class);
        rule.setPerformanceLoggerContext(context);
        TestRun testRun = new TestRun();
        when(context.getCurrentRun()).thenReturn(testRun);
        when(context.getAverageDurationForMethod(testMethodName)).thenReturn(50.0);
        Description description = mock(Description.class);
        when(description.getDisplayName()).thenReturn(testMethodName);
        Statement base = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Thread.sleep(50);
            }
        };
        Statement statement = rule.apply(base, description);

        // when
        statement.evaluate();

        // then
        assertEquals(1, testRun.getPerformanceMeasures().size());
        long percentage = 20L;
        double percentageDouble = percentage / 100.0;
        long lowerBound = (long)((1.0 - percentageDouble) * 50L);
        long upperBound = (long)((1.0 + percentageDouble) * 50L);
        PerformanceMeasure performanceMeasure = testRun.getPerformanceMeasures().get(0);
        Long duration = performanceMeasure.getDuration();
        assertTrue(duration >= lowerBound);
        assertTrue(duration <= upperBound);
        assertEquals(testMethodName, performanceMeasure.getTestMethodName());
    }

    @Test(expected = PerformanceRegressionException.class)
    public void applyTestMethodThrowsException() throws Throwable {
        // given
        final String testMethodName = "testMethodName";
        PerformanceLoggerContext context = mock(PerformanceLoggerContext.class);
        rule.setPerformanceLoggerContext(context);
        TestRun testRun = new TestRun();
        when(context.getCurrentRun()).thenReturn(testRun);
        when(context.getAverageDurationForMethod(testMethodName)).thenReturn(50.0);
        Description description = mock(Description.class);
        when(description.getDisplayName()).thenReturn(testMethodName);
        Statement base = new Statement() {
            @Override
            public void evaluate() {
                throw new RuntimeException("This should be detected and processed as well");
            }
        };
        Statement statement = rule.apply(base, description);

        // when
        statement.evaluate();
    }
}