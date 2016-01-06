package io.swagger.models.auth;

import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class InTest {

    @Test
    public void testForValue() {
        assertEquals(In.forValue("header"), In.HEADER, "Must return HEADER  for header");
        assertNull(In.forValue("unknown"), "Must return null for unknown values");
    }

    @Test
    public void testToValue() {
        assertEquals(In.HEADER.toValue(), "header", "Must return HEADER  for header");
    }

    @Test
    public void testValues() {
        assertEquals(In.valueOf("HEADER"), In.HEADER, "Must return HEADER  for HEADER");
        assertTrue(Arrays.asList(In.values()).contains(In.HEADER), "Values must contain HEADER");
    }
}
