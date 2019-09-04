package nl.neurone.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class PerformanceMeasure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String testMethodName;
    private Long duration;
    @ToString.Exclude
    private TestRun testRun;

    // TODO: remove when JPA is used and an id is generated automatically
    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
        setId((long) testRun.getPerformanceMeasures().size());
    }
}
