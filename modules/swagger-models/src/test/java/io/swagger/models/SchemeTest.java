package io.swagger.models;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class SchemeTest {

    @Test
    public void testForValue() {
        assertEquals(Scheme.forValue("http"), Scheme.HTTP, "The forValue must return HTTP for http");
        assertNull(Scheme.forValue("unknown"), "The forValue must return null for unknown values");
    }

    @Test
    public void testToValue() {
        assertEquals(Scheme.HTTP.toValue(), "http", "The toValue must return http for HTTP");
    }

    @Test
    public void testValueOf() {
        assertEquals(Scheme.valueOf("HTTP"), Scheme.HTTP, "The valueOf must return HTTP for http");
    }
}
