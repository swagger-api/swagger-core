package io.swagger.models;

import io.swagger.models.refs.RefFormat;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RefPathTest {

    @Test
    public void testConstructor() {
        //given
        RefPath refPath = new RefPath("ref");

        //then
        assertEquals(refPath.get$ref(), "#/paths/ref", "The ref value must respect the format");
        assertEquals(refPath.getRefFormat(), RefFormat.INTERNAL, "The format must be INTERNAL");
    }
}
