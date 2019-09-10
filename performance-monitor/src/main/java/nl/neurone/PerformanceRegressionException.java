package nl.neurone;

class PerformanceRegressionException extends Exception {
    private static final String GENERIC_MESSAGE = "An exception occured during execution of test method through the PerformanceMonitorRule: ";

    PerformanceRegressionException(String msg) {
        super(GENERIC_MESSAGE + msg);
    }

    PerformanceRegressionException(Exception e) {
        super(GENERIC_MESSAGE + e.getMessage(), e);
    }
}
