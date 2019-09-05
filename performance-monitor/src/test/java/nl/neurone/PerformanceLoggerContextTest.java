package nl.neurone;

import org.junit.Test;

import static org.junit.Assert.*;

public class PerformanceLoggerContextTest {

    // TODO: Decide if other methods need to be tested (and if so, do it!)

    private PerformanceLoggerContext context = PerformanceLoggerContext.getInstance();

    @Test
    public void getDriver() {
        assertEquals("dbDriver", context.getDriver());
    }

    @Test
    public void getUrl() {
        assertEquals("jdbcUrl", context.getUrl());
    }

    @Test
    public void getUser() {
        assertEquals("user", context.getUser());
    }

    @Test
    public void getPassword() {
        assertEquals("password", context.getPassword());
    }
}