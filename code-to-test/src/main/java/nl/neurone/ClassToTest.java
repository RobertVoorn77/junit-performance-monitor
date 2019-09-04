package nl.neurone;

public class ClassToTest {

    public void fastMethod() {
        sleep(50);
    }

    public void slowMethod() {
        sleep(5000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
