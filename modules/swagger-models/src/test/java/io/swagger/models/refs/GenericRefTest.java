package io.swagger.models.refs;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by russellb337 on 7/16/15.
 */
public class GenericRefTest {

    @Test
    public void testIdentifyingUrlRefs() throws Exception {
        assertRefFormat(new GenericRef(RefType.DEFINITION, "http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing"), RefFormat.URL);
    }

    @Test
    public void testIdentifyInternalRefs() throws Exception {
        assertRefFormat(new GenericRef(RefType.DEFINITION, "#/definitions/foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "#/parameters/foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "Foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "Foo"), RefFormat.INTERNAL);
    }

    @Test
    public void testIdentifyRelativeRefs() throws Exception {
        assertRefFormat(new GenericRef(RefType.DEFINITION, "./path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "./path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "../path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "../path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "./path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "./path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "../path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "../path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "/path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "/path/to/model.json#/thing"), RefFormat.RELATIVE);
    }

    @Test
    public void testGetFullRef() throws Exception {
        assertRefStringIsUnchanged(RefType.DEFINITION, "./path/to/model.json");
        assertRefStringIsUnchanged(RefType.DEFINITION, "./path/to/model.json#/thing");
        assertRefStringIsUnchanged(RefType.DEFINITION, "/path/to/model.json");
        assertRefStringIsUnchanged(RefType.DEFINITION, "/path/to/model.json#/thing");
        assertRefStringIsUnchanged(RefType.PARAMETER, "./path/to/parameters.json#/param");
        assertRefStringIsUnchanged(RefType.PARAMETER, "./path/to/parameters.json#/param");
        assertRefStringIsUnchanged(RefType.DEFINITION, "#/definitions/foo");
        assertRefStringIsUnchanged(RefType.PARAMETER, "#/parameters/foo");
        assertRefStringIsUnchanged(RefType.DEFINITION, "http://my.company.com/models/model.json");
        assertRefStringIsUnchanged(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing");
        assertRefStringIsUnchanged(RefType.PARAMETER, "http://my.company.com/models/model.json");
        assertRefStringIsUnchanged(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing");

        assertRefString(RefType.DEFINITION, "foo", "#/definitions/foo");
        assertRefString(RefType.PARAMETER, "foo", "#/parameters/foo");
    }

    @Test
    public void testGetSimpleRef() throws Exception {
        assertSimpleRefMatchesRef(RefType.DEFINITION, "./path/to/model.json");
        assertSimpleRefMatchesRef(RefType.DEFINITION, "./path/to/model.json#/thing");
        assertSimpleRefMatchesRef(RefType.DEFINITION, "/path/to/model.json");
        assertSimpleRefMatchesRef(RefType.DEFINITION, "/path/to/model.json#/thing");
        assertSimpleRefMatchesRef(RefType.PARAMETER, "./path/to/parameters.json#/param");
        assertSimpleRefMatchesRef(RefType.PARAMETER, "./path/to/parameters.json#/param");

        assertSimpleRefMatchesRef(RefType.DEFINITION, "http://my.company.com/models/model.json");
        assertSimpleRefMatchesRef(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing");
        assertSimpleRefMatchesRef(RefType.PARAMETER, "http://my.company.com/models/model.json");
        assertSimpleRefMatchesRef(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing");

        assertSimpleRef(RefType.DEFINITION, "#/definitions/foo", "foo");
        assertSimpleRef(RefType.PARAMETER, "#/parameters/foo", "foo");
        assertSimpleRef(RefType.DEFINITION, "foo", "foo");
        assertSimpleRef(RefType.PARAMETER, "foo", "foo");
    }

    @Test
    public void testDontAllowInternalPathRefs() throws Exception {
        assertExceptionThrownInConstructor(RefType.PATH);
    }

    @Test
    public void testDontAllowInternalResponseRefs() throws Exception {
        assertExceptionThrownInConstructor(RefType.RESPONSE);
    }

    public void assertRefStringIsUnchanged(RefType refType, String refStr) {
        assertRefString(refType, refStr, refStr);
    }

    public void assertRefString(RefType refType, String refStr, String expectedRefStr) {
        GenericRef refObj = new GenericRef(refType, refStr);
        assertEquals(refObj.getRef(), expectedRefStr);
    }

    public void assertSimpleRefMatchesRef(RefType refType, String refStr) {
        assertSimpleRef(refType, refStr, refStr);
    }

    public void assertSimpleRef(RefType refType, String refStr, String expectedSimpleRef) {
        GenericRef refObj = new GenericRef(refType, refStr);
        assertEquals(refObj.getSimpleRef(), expectedSimpleRef);
    }

    public void assertRefFormat(GenericRef actual, RefFormat expectedFormat) {
        assertEquals(actual.getFormat(), expectedFormat);
    }

    public void assertExceptionThrownInConstructor(RefType path) {
        boolean exceptionThrown = false;
        try {
            new GenericRef(path, "#/paths/foo");
        } catch (Throwable t) {
            exceptionThrown = true;
        }
    }
}
