package nl.neurone;

class ClassToTest {

    void fastMethod() {
        sleep(50);
    }

    void slowMethod() {
        sleep(2500);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Throwable e) {
            System.out.println("catch 2");
            e.printStackTrace();
        }
    }
}
