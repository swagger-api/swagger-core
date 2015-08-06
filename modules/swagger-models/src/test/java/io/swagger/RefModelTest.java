package io.swagger;

import static org.testng.Assert.assertEquals;

import io.swagger.models.RefModel;
import io.swagger.models.refs.RefFormat;

import org.testng.annotations.Test;

public class RefModelTest {

    private void assertRefFormat(RefModel ref, RefFormat expectedFormat) {
        assertEquals(ref.getRefFormat(), expectedFormat);
    }

    @Test(description = "it should correctly identify ref formats")
    public void identifyRefFormats() {
        assertRefFormat(new RefModel("http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new RefModel("http://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new RefModel("./models/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new RefModel("./models/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new RefModel("#/definitions/foo"), RefFormat.INTERNAL);
        assertRefFormat(new RefModel("foo"), RefFormat.INTERNAL);
    }
}
