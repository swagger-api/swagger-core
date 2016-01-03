package io.swagger.models;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TagTest {

    @Test
    public void testToString() {
        //given
        Tag tag = new Tag();
        String vendorName = "x-vendor";
        String value = "value";

        //when
        tag.setVendorExtension(vendorName, value);

        //then
        assertEquals(tag.getVendorExtensions().get(vendorName), value, "Must be able to retrieve the same value from the map");
        assertTrue(tag.toString().contains("extensions:{x-vendor=value}}"));
    }
}
