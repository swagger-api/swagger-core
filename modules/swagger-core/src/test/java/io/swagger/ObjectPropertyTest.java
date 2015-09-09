package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.testng.annotations.Test;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;

public class ObjectPropertyTest {
    @Test (description = "convert a model with object properties")
    public void readModelWithObjectProperty() throws IOException {
        String json = "{" +
                "   \"properties\":{" +
                "      \"id\":{" +
                "         \"type\":\"string\"" +
                "      }," +
                "      \"someObject\":{" +
                "         \"type\":\"object\"," +
                "        \"x-foo\": \"vendor x\"," +
                "         \"properties\":{" +
                "            \"innerId\":{" +
                "               \"type\":\"string\"" +
                "            }" +
                "         }" +
                "      }" +
                "   }" +
                "}";

        ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);

        Property p = model.getProperties().get("someObject");
        assertTrue(p instanceof ObjectProperty);

        ObjectProperty op = (ObjectProperty) p;

        Property sp = op.getProperties().get("innerId");
        assertTrue(sp instanceof StringProperty);

        assertTrue(op.getVendorExtensions() != null);
        assertNotNull(op.getVendorExtensions().get("x-foo"));
        assertEquals(op.getVendorExtensions().get("x-foo"), "vendor x");
    }
}
