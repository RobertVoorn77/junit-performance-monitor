package nl.neurone;

import nl.neurone.model.PerformanceMeasure;
import nl.neurone.model.TestRun;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class PerformanceMonitorRuleTest {

    private static TestRun testRun;
    private PerformanceLoggerContext context = mock(PerformanceLoggerContext.class);
    private PerformanceMonitorRule rule = new PerformanceMonitorRule();
    private Description description = mock(Description.class);
    private static final String TEST_METHOD_NAME = "testMethodName";

    @SuppressWarnings("WeakerAccess")
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        rule.setPerformanceLoggerContext(context);
        testRun = new TestRun();
        when(context.getCurrentRun()).thenReturn(testRun);
        when(context.getAverageDurationForMethod(TEST_METHOD_NAME)).thenReturn(50.0);
        when(description.getDisplayName()).thenReturn(TEST_METHOD_NAME);
    }

    @Test
    public void applyTooMuchPerformanceRegressionThrowsException() throws Throwable {
        // given
        expectedException.expect(PerformanceRegressionException.class);
        Statement statement = rule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Thread.sleep(100);
            }
        }, description);

        // when
        statement.evaluate();
    }

    @Test
    public void applyNoPerformanceRegression() throws Throwable {
        // given
        Statement statement = rule.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Thread.sleep(50);
            }
        }, description);

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
        assertEquals(TEST_METHOD_NAME, performanceMeasure.getTestMethodName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyTestMethodThrowsException() throws Throwable {
        // given
        Statement base = new Statement() {
            @Override
            public void evaluate() {
                throw new IllegalArgumentException("This should be detected and processed as well");
            }
        };
        Statement statement = rule.apply(base, description);

        // when
        statement.evaluate();
    }

    @Test
    public void testMaxDurationExceeded() throws Throwable {
        // given
        expectedException.expect(PerformanceRegressionException.class);
        expectedException.expectMessage("Method duration exceeded the maximum of 10");
        Statement base = new Statement() {
            @Override
            public void evaluate() throws InterruptedException {
                Thread.sleep(100);
            }
        };
        MaxDuration maxDuration = mock(MaxDuration.class);
        when(maxDuration.millis()).thenReturn(10L);
        when(description.getAnnotation(MaxDuration.class)).thenReturn(maxDuration);
        Statement statement = rule.apply(base, description);

        // when
        statement.evaluate();
    }
}