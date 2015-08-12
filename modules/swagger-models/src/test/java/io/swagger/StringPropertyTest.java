package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import io.swagger.models.properties.StringProperty;

import org.testng.annotations.Test;

public class StringPropertyTest {
    private static final String PROP_1 = "prop1";
    private static final String PROP_2 = "prop2";

    @Test
    public void testEquals() {
        assertNotEquals(new StringProperty(StringProperty.Format.BYTE), new StringProperty(StringProperty.Format.URL));
        assertEquals(new StringProperty(StringProperty.Format.BYTE), new StringProperty(StringProperty.Format.BYTE));

        final StringProperty prop1 = new StringProperty();
        prop1.setName(PROP_1);
        prop1.setRequired(true);

        final StringProperty prop2 = new StringProperty();
        prop2.setName(PROP_2);
        assertNotEquals(prop1, prop2);

        prop2.setName(PROP_1);
        prop2.setRequired(true);
        assertEquals(prop1, prop2);
    }
}
