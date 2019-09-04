package nl.neurone;

import org.junit.Rule;
import org.junit.Test;

public class TestClassToTest {

    @Rule
    public PerformanceMonitorRule rule = new PerformanceMonitorRule();

    ClassToTest classToTest = new ClassToTest();

    @Test
    @MaxDuration(millis = 1)
    public void testFastMethod() {
        classToTest.fastMethod();
    }

    @Test
    @MaxDuration(millis = 1000)
    public void testSlowMethod() {
        classToTest.slowMethod();
    }

}