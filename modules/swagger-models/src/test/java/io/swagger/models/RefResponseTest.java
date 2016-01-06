package io.swagger.models;

import io.swagger.models.refs.RefFormat;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RefResponseTest {

    @Test
    public void testConstructor() {
        //given
        RefResponse RefResponse = new RefResponse("ref");

        //then
        assertEquals(RefResponse.get$ref(), "#/responses/ref", "The ref value must respect the format");
        assertEquals(RefResponse.getRefFormat(), RefFormat.INTERNAL, "The format must be INTERNAL");
    }
}
