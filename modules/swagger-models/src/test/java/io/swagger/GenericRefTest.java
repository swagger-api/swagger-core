package io.swagger;

import static org.testng.Assert.assertEquals;

import io.swagger.models.refs.GenericRef;
import io.swagger.models.refs.RefFormat;
import io.swagger.models.refs.RefType;

import org.testng.annotations.Test;

public class GenericRefTest {

    private void assertRefFormat(GenericRef ref, RefFormat expectedFormat) {
        assertEquals(ref.getFormat(), expectedFormat);
    }

    @Test(description = "it should correctly identify URL refs")
    public void identifyURLRefs() {
        assertRefFormat(new GenericRef(RefType.DEFINITION, "http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "https://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "https://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "https://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "https://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "http://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "https://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "https://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PATH, "http://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PATH, "http://my.company.com/models/model.json#/thing"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PATH, "https://my.company.com/models/model.json"), RefFormat.URL);
        assertRefFormat(new GenericRef(RefType.PATH, "https://my.company.com/models/model.json#/thing"), RefFormat.URL);
    }

    @Test(description = "it should correctly identify internal refs")
    public void identifyInternalRefs() {
        assertRefFormat(new GenericRef(RefType.DEFINITION, "#/definitions/foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "#/parameters/foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "#/responses/foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.PATH, "#/paths/foo"), RefFormat.INTERNAL);

        assertRefFormat(new GenericRef(RefType.PARAMETER, "Foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "Foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "Foo"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.PATH, "Foo"), RefFormat.INTERNAL);

        assertRefFormat(new GenericRef(RefType.PARAMETER, "httpNotUrl"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "httpNotUrl"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "httpNotUrl"), RefFormat.INTERNAL);
        assertRefFormat(new GenericRef(RefType.PATH, "httpNotUrl"), RefFormat.INTERNAL);
    }

    @Test(description = "it should correctly identify relative refs")
    public void identifyRelativeRefs() {
        assertRefFormat(new GenericRef(RefType.DEFINITION, "./path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "./path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "../path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.DEFINITION, "../path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "./path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "./path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "../path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PARAMETER, "../path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "./path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "./path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "../path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.RESPONSE, "../path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PATH, "./path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PATH, "./path/to/model.json#/thing"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PATH, "../path/to/model.json"), RefFormat.RELATIVE);
        assertRefFormat(new GenericRef(RefType.PATH, "../path/to/model.json#/thing"), RefFormat.RELATIVE);
    }

    private void assertRefStringIsUnchanged(RefType refType, String refStr) {
        assertRefString(refType, refStr, refStr);
    }

    private void assertRefString(RefType refType, String refStr, String expectedRefStr) {
        final GenericRef refObj = new GenericRef(refType, refStr);
        assertEquals(refObj.getRef(), expectedRefStr);
    }

    private void assertSimpleRefMatchesRef(RefType refType, String refStr) {
        assertSimpleRef(refType, refStr, refStr);
    }

    private void assertSimpleRef(RefType refType, String refStr, String expectedSimpleRef) {
        final GenericRef refObj = new GenericRef(refType, refStr);
        assertEquals(refObj.getSimpleRef(), expectedSimpleRef);
    }

    @Test(description = "it should give back the right ref string")
    public void giveBackRightRefString() {
        assertRefStringIsUnchanged(RefType.DEFINITION, "./path/to/model.json");
        assertRefStringIsUnchanged(RefType.DEFINITION, "./path/to/model.json#/thing");
        assertRefStringIsUnchanged(RefType.PARAMETER, "./path/to/parameters.json");
        assertRefStringIsUnchanged(RefType.PARAMETER, "./path/to/parameters.json#/thing");
        assertRefStringIsUnchanged(RefType.PATH, "./path/to/parameters.json");
        assertRefStringIsUnchanged(RefType.PATH, "./path/to/parameters.json#/thing");
        assertRefStringIsUnchanged(RefType.RESPONSE, "./path/to/parameters.json");
        assertRefStringIsUnchanged(RefType.RESPONSE, "./path/to/parameters.json#/thing");

        assertRefStringIsUnchanged(RefType.DEFINITION, "#/definitions/foo");
        assertRefStringIsUnchanged(RefType.PARAMETER, "#/parameters/foo");
        assertRefStringIsUnchanged(RefType.PATH, "#/paths/foo");
        assertRefStringIsUnchanged(RefType.RESPONSE, "#/responses/foo");

        assertRefStringIsUnchanged(RefType.DEFINITION, "http://my.company.com/models/model.json");
        assertRefStringIsUnchanged(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing");
        assertRefStringIsUnchanged(RefType.PARAMETER, "http://my.company.com/models/model.json");
        assertRefStringIsUnchanged(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing");
        assertRefStringIsUnchanged(RefType.RESPONSE, "http://my.company.com/models/model.json");
        assertRefStringIsUnchanged(RefType.RESPONSE, "http://my.company.com/models/model.json#/thing");
        assertRefStringIsUnchanged(RefType.PATH, "http://my.company.com/models/model.json");
        assertRefStringIsUnchanged(RefType.PATH, "http://my.company.com/models/model.json#/thing");

        assertRefString(RefType.DEFINITION, "foo", "#/definitions/foo");
        assertRefString(RefType.PARAMETER, "foo", "#/parameters/foo");
        assertRefString(RefType.RESPONSE, "foo", "#/responses/foo");
        assertRefString(RefType.PATH, "foo", "#/paths/foo");

        assertRefString(RefType.DEFINITION, "httpNotUrl", "#/definitions/httpNotUrl");
        assertRefString(RefType.PARAMETER, "httpNotUrl", "#/parameters/httpNotUrl");
        assertRefString(RefType.RESPONSE, "httpNotUrl", "#/responses/httpNotUrl");
        assertRefString(RefType.PATH, "httpNotUrl", "#/paths/httpNotUrl");
    }

    @Test(description = "it should give back the right simple ref")
    public void giveBackRightSimpleRef() {
        assertSimpleRefMatchesRef(RefType.DEFINITION, "./path/to/model.json");
        assertSimpleRefMatchesRef(RefType.DEFINITION, "./path/to/model.json#/thing");
        assertSimpleRefMatchesRef(RefType.PARAMETER, "./path/to/parameters.json#/param");
        assertSimpleRefMatchesRef(RefType.PARAMETER, "./path/to/parameters.json#/param");
        assertSimpleRefMatchesRef(RefType.RESPONSE, "./path/to/parameters.json#/param");
        assertSimpleRefMatchesRef(RefType.RESPONSE, "./path/to/parameters.json#/param");
        assertSimpleRefMatchesRef(RefType.PATH, "./path/to/parameters.json#/param");
        assertSimpleRefMatchesRef(RefType.PATH, "./path/to/parameters.json#/param");

        assertSimpleRefMatchesRef(RefType.DEFINITION, "http://my.company.com/models/model.json");
        assertSimpleRefMatchesRef(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing");
        assertSimpleRefMatchesRef(RefType.PARAMETER, "http://my.company.com/models/model.json");
        assertSimpleRefMatchesRef(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing");
        assertSimpleRefMatchesRef(RefType.PATH, "http://my.company.com/models/model.json");
        assertSimpleRefMatchesRef(RefType.PATH, "http://my.company.com/models/model.json#/thing");
        assertSimpleRefMatchesRef(RefType.RESPONSE, "http://my.company.com/models/model.json");
        assertSimpleRefMatchesRef(RefType.RESPONSE, "http://my.company.com/models/model.json#/thing");

        assertSimpleRef(RefType.DEFINITION, "#/definitions/foo", "foo");
        assertSimpleRef(RefType.PARAMETER, "#/parameters/foo", "foo");
        assertSimpleRef(RefType.RESPONSE, "#/responses/foo", "foo");
        assertSimpleRef(RefType.PATH, "#/paths/foo", "foo");

        assertSimpleRef(RefType.DEFINITION, "foo", "foo");
        assertSimpleRef(RefType.PARAMETER, "foo", "foo");
        assertSimpleRef(RefType.RESPONSE, "foo", "foo");
        assertSimpleRef(RefType.PATH, "foo", "foo");

        assertSimpleRef(RefType.DEFINITION, "httpNotUrl", "httpNotUrl");
        assertSimpleRef(RefType.PARAMETER, "httpNotUrl", "httpNotUrl");
        assertSimpleRef(RefType.RESPONSE, "httpNotUrl", "httpNotUrl");
        assertSimpleRef(RefType.PATH, "httpNotUrl", "httpNotUrl");
    }
}
