package io.swagger;

import static org.testng.Assert.assertEquals;

import io.swagger.models.parameters.RefParameter;
import io.swagger.models.refs.RefFormat;

import org.testng.annotations.Test;

public class RefParameterTest {

    private void assertRefFormat(RefParameter ref, RefFormat expectedFormat) {
        assertEquals(ref.getRefFormat(), expectedFormat);
    }

    @Test(description = "it should correctly identify ref formats")
    public void identifyRefFormats() {
        assertRefFormat(new RefParameter("http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new RefParameter("http://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new RefParameter("./models/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new RefParameter("./models/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new RefParameter("#/parameters/foo"), RefFormat.INTERNAL);
        assertRefFormat(new RefParameter("foo"), RefFormat.INTERNAL);
    }
}
