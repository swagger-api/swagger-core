package io.swagger.models.refs;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class RefTypeTest {
    @Test
    public void testValues() {
        // when
        List<RefType> values = Arrays.asList(RefType.values());

        // then
        assertTrue(values.contains(RefType.DEFINITION), "The values must contain DEFINITION");
    }

    @Test
    public void testValueOf() {
        // when
        RefType value = RefType.valueOf("DEFINITION");

        // then
        assertEquals(value, RefType.DEFINITION, "The value for 'DEFINITION' is DEFINITION");
    }
}
