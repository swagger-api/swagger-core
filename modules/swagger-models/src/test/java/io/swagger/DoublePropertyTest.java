package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import io.swagger.models.properties.DoubleProperty;

import org.testng.annotations.Test;

public class DoublePropertyTest {
    private static final String PROP_1 = "prop1";
    private static final String PROP_2 = "prop2";

    @Test
    public void testEquals() {
        final DoubleProperty prop1 = new DoubleProperty();
        prop1.setName(PROP_1);
        prop1.setRequired(true);

        final DoubleProperty prop2 = new DoubleProperty();
        prop2.setName(PROP_2);
        assertNotEquals(prop1, prop2);

        prop2.setName(PROP_1);
        prop2.setRequired(true);
        assertEquals(prop1, prop2);
    }
}
