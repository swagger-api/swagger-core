package io.swagger.models.parameters;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RefParameterTest {

    private RefParameter parameter;

    @BeforeMethod
    public void setup() {
        parameter = new RefParameter("ref");
    }

    @Test
    public void testAsDefault() {
        //when
        parameter.asDefault("ref");

        //then
        assertEquals(parameter.get$ref(), "#/parameters/ref", "Must respect the ref format");
        assertEquals(parameter.getSimpleRef(), "ref", "Simple ref must be the same as the set one");
    }

    @Test
    public void testGetRequired() {
        assertFalse(parameter.getRequired(), "A new instance must have the required value at false");
    }

    @Test
    public void testIsType() {
        assertTrue(RefParameter.isType("$ref", "format"), "iType must be true for $ref and format");
        assertFalse(RefParameter.isType("$ref0", "format"), "isType must be false for $ref0 and format");
    }
}
