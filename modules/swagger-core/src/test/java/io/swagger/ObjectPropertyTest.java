package io.swagger;

import io.swagger.models.ModelImpl;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

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

    @Test(description = "it should deserialize a property with enum and default")
    public void deserializeNumberType() throws IOException {
        final String yamlNumberWithFormat = "---\n" +
                "type: \"object\"\n" +
                "properties:\n" +
                "  id:\n" +
                "    type: \"number\"\n" +
                "    format: \"double\"\n" +
                "    enum: [1.0, 2.0]\n" +
                "    minimum: 1.0\n" +
                "    maximum: 1.0\n" +
                "    default: 1.0";

        final ModelImpl modelWithFormat = io.swagger.util.Yaml.mapper().readValue(yamlNumberWithFormat, ModelImpl.class);

        final DoubleProperty propertyWithFormat = (DoubleProperty)modelWithFormat.getProperties().get("id");
        assertNotNull(propertyWithFormat);
        assertNotNull(propertyWithFormat.getEnum());
        assertEquals(propertyWithFormat.getMinimum(), new BigDecimal("1.0"));
        assertEquals(propertyWithFormat.getMaximum(), new BigDecimal("1.0"));
        assertEquals(propertyWithFormat.getEnum().size(), 2);
        assertEquals(propertyWithFormat.getEnum().get(0), 1.0d);
        assertEquals(propertyWithFormat.getEnum().get(1), 2.0d);
        assertEquals(propertyWithFormat.getDefault(), 1.0d);

        final String yamlNumberWithoutFormat = "---\n" +
                "type: \"object\"\n" +
                "properties:\n" +
                "  id:\n" +
                "    type: \"number\"\n" +
                "    enum: [1.0, 2.0]\n" +
                "    default: 1.0";

        final ModelImpl modelWithoutFormat = io.swagger.util.Yaml.mapper().readValue(yamlNumberWithoutFormat, ModelImpl.class);

        // NOTE: Expect to be a DoubleProperty because DecimalProperty didn't have enum or default attributes
        final DoubleProperty propertyWithoutFormat = (DoubleProperty)modelWithoutFormat.getProperties().get("id");
        assertNotNull(propertyWithoutFormat);
        assertEquals(propertyWithoutFormat.getMinimum(), new BigDecimal("1.0"));
        assertEquals(propertyWithoutFormat.getMaximum(), new BigDecimal("1.0"));
        assertNotNull(propertyWithoutFormat.getEnum());
        assertEquals(propertyWithoutFormat.getEnum().size(), 2);
        assertEquals(propertyWithoutFormat.getEnum().get(0), 1.0d);
        assertEquals(propertyWithoutFormat.getEnum().get(1), 2.0d);
        assertEquals(propertyWithoutFormat.getDefault(), 1.0d);
    }

}
