package nl.neurone;

import org.junit.Rule;
import org.junit.Test;

/***
 * Second test class to see if the PerformanceLoggerContext is loaded twice (if so, this is not a good solution because
 * it will cause a serious performance hit for the tests and defeats the purpose of this project
 */
public class TestClassToTest2 {
    @Rule
    public PerformanceMonitorRule rule = new PerformanceMonitorRule();

    private ClassToTest classToTest = new ClassToTest();

    @Test
    @MaxDuration(millis = 100)
    public void testFastMethod() {
        classToTest.fastMethod();
    }

    @Test
    @MaxDuration(millis = 3000)
    public void testSlowMethod() {
        classToTest.slowMethod();
    }

}