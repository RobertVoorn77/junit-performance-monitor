package nl.neurone;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class TestClassToTest {
//    private List<String> failedTestMethodNames = new ArrayList<>();

//    @Rule
//    public TestWatcher watchman= new TestWatcher() {
//        @Override
//        protected void failed(Throwable e, Description description) {
//            String displayName = description.getDisplayName();
//            failedTestMethodNames.add(displayName);
//        }
//    };

    @Rule
    public PerformanceMonitorRule rule = new PerformanceMonitorRule();

    private ClassToTest classToTest = new ClassToTest();

    @Test
    @Ignore // TODO: Get these working, for now, just use them for manual testing functionality
    @MaxDuration(millis = 10)
    public void testFastMethodThrowsException() {
        classToTest.fastMethod();
    }

    @Test
    @Ignore // TODO: Get these working, for now, just use them for manual testing functionality
    @MaxDuration(millis = 1000)
    public void testSlowMethodThrowsException() {
        classToTest.slowMethod();
//        assertTrue(failedTestMethodNames.contains("testSlowMethodThrowsException(nl.neurone.TestClassToTest)"));
//        assertTrue(rule.isPerformanceExceptionThrown());
    }

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