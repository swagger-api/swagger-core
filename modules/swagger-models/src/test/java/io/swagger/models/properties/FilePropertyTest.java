package io.swagger.models.properties;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class FilePropertyTest {

    @Test
    public void testIsType() {
        //when
        boolean isType = FileProperty.isType("file", "format");

        //then
        assertTrue(isType, "isType must return true for file and format");
    }
}
