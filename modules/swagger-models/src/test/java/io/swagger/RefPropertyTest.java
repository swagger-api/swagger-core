package io.swagger;

import static org.testng.Assert.assertEquals;

import io.swagger.models.properties.RefProperty;
import io.swagger.models.refs.RefFormat;

import org.testng.annotations.Test;

public class RefPropertyTest {
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
}
