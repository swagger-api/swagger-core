package io.swagger.models;

import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.Property;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ResponseTest {
    @Test
    public void testExample() {
        // given
        Response response = new Response();
        String type = "type";
        Object example = "example";

        // when
        response.example(type, example);

        // then
        assertEquals(example, response.getExamples().get(type),
                "The retrieved example must be the same as the set one");
    }

    @Test
    public void testHeader() {
        // given
        Response response = new Response();
        String name = "name";
        Property property = new BooleanProperty();

        // when
        response.header(name, property);

        // then
        assertEquals(response.getHeaders().get("name"), property,
                "The retrieved property must be the same as the set one");
    }

    @Test
    public void testVendorExtension() {
        // given
        Response response = new Response();
        String vendorName = "x-vendor";
        String value = "value";

        // when
        response.vendorExtension(vendorName, value);

        // then
        assertEquals(response.getVendorExtensions().get(vendorName), value,
                "Must be able to retrieve the same value from the map");
    }
}
