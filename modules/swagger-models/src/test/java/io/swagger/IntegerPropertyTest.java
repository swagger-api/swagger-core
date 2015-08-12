package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import io.swagger.models.properties.IntegerProperty;

import org.testng.annotations.Test;

public class IntegerPropertyTest {
    private static final String PROP_1 = "prop1";
    private static final String PROP_2 = "prop2";

    @Test
    public void testEquals() {
        final IntegerProperty prop1 = new IntegerProperty();
        prop1.setName(PROP_1);
        prop1.setRequired(true);

        final IntegerProperty prop2 = new IntegerProperty();
        prop2.setName(PROP_2);
        assertNotEquals(prop1, prop2);

        prop2.setName(PROP_1);
        prop2.setRequired(true);
        assertEquals(prop1, prop2);
    }
}
