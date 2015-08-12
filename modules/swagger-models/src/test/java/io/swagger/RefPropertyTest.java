package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.refs.RefFormat;

import org.testng.annotations.Test;

public class RefPropertyTest {
    private static final String PROP_1 = "prop1";
    private static final String PROP_2 = "prop2";

    private void assertRefFormat(RefProperty ref, RefFormat expectedFormat) {
        assertEquals(ref.getRefFormat(), expectedFormat);
    }

    @Test(description = "it should correctly identify ref formats")
    public void identifyRefFormats() {
        assertRefFormat(new RefProperty("http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new RefProperty("http://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new RefProperty("./models/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new RefProperty("./models/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new RefProperty("#/definitions/foo"), RefFormat.INTERNAL);
        assertRefFormat(new RefProperty("foo"), RefFormat.INTERNAL);
    }

    @Test
    public void testEquals() {
        final RefProperty prop1 = new RefProperty();
        prop1.setName(PROP_1);
        prop1.setRequired(true);

        final RefProperty prop2 = new RefProperty();
        prop2.setName(PROP_2);
        assertNotEquals(prop1, prop2);

        prop2.setName(PROP_1);
        prop2.setRequired(true);
        assertEquals(prop1, prop2);
    }
}
