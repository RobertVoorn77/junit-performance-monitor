package nl.neurone.repositories;

public interface TestRunRepository<T> {
    void save(T testRun);

    double getAverageDurationForMethod(String testMethodName);
}
