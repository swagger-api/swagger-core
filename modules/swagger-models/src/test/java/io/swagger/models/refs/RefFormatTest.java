package io.swagger.models.refs;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class RefFormatTest {
    @Test
    public void testValues() {
        // when
        List<RefFormat> values = Arrays.asList(RefFormat.values());

        // then
        assertTrue(values.contains(RefFormat.INTERNAL), "The values must contain INTERNAL");
    }

    @Test
    public void testValueOf() {
        // when
        RefFormat refFormat = RefFormat.valueOf("INTERNAL");

        // then
        assertEquals(refFormat, RefFormat.INTERNAL, "The value for 'INTERNAL' is INTERNAL");
    }
}
