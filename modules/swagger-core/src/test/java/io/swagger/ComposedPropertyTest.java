package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.testng.annotations.Test;

import io.swagger.models.ModelImpl;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.ComposedProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;

public class ComposedPropertyTest {

    @Test(description = "convert a model with object properties")
    public void readModelWithObjectProperty() throws IOException {
        String json = "{\n" +
                      "  \"properties\":{\n" +
                      "    \"id\":{\n" +
                      "      \"type\":\"string\"\n" +
                      "    },\n" +
                      "    \"someObject\":{\n" +
                      "      \"type\":\"object\",\n" +
                      "      \"x-foo\": \"vendor x\",\n" +
                      "      \"allOf\":[\n" +
                      "        {\n" +
                      "          \"type\":\"object\",\n" +
                      "          \"properties\":{\n" +
                      "            \"innerId\":{\n" +
                      "              \"type\":\"string\"\n" +
                      "            }\n" +
                      "          }\n" +
                      "        },{\n" +
                      "          \"type\":\"object\",\n" +
                      "          \"properties\":{\n" +
                      "            \"innerLength\":{\n" +
                      "            \"type\":\"number\"\n" +
                      "            }\n" +
                      "          }\n" +
                      "        }\n" +
                      "      ]\n" +
                      "    }\n" +
                      "  }\n" +
                      "}";

        ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);

        Property p = model.getProperties().get("someObject");
        assertTrue(p instanceof ComposedProperty);

        ComposedProperty cp = (ComposedProperty) p;

        Property op0 = cp.getAllOf().get(0);
        Property op1 = cp.getAllOf().get(1);

        assertTrue(op0 instanceof ObjectProperty);
        assertTrue(op1 instanceof ObjectProperty);

        Property sp = ((ObjectProperty) op0).getProperties().get("innerId");
        Property np = ((ObjectProperty) op1).getProperties().get("innerLength");

        assertTrue(sp instanceof StringProperty);
        assertTrue(np instanceof AbstractNumericProperty);

        assertTrue(cp.getVendorExtensions() != null);
        assertNotNull(cp.getVendorExtensions().get("x-foo"));
        assertEquals(cp.getVendorExtensions().get("x-foo"), "vendor x");
    }
}
