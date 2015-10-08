package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;

import io.swagger.models.properties.ArrayProperty;

import org.testng.annotations.Test;

public class ArrayPropertyTest {
    private static final String PROP_1 = "prop1";
    private static final String PROP_2 = "prop2";

    @Test
    public void testEquals() {
        final ArrayProperty prop1 = new ArrayProperty();
        prop1.setName(PROP_1);
        prop1.setRequired(true);

        final ArrayProperty prop2 = new ArrayProperty();
        prop2.setName(PROP_2);
        assertNotEquals(prop1, prop2);

        prop2.setName(PROP_1);
        prop2.setRequired(true);
        assertEquals(prop1, prop2);
    }

    @Test
    public void testSetNullUniqueItems() {
        final ArrayProperty prop = new ArrayProperty();
        prop.setUniqueItems(null);

        assertNull(prop.getUniqueItems());
    }
}
