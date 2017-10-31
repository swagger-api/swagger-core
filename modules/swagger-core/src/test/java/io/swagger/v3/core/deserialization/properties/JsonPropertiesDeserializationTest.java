package io.swagger.v3.core.deserialization.properties;

import io.swagger.v3.core.util.TestUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


public class JsonPropertiesDeserializationTest {

    @Test (description = "should deserialize a string property with constraints")
    public void testDeserializeConstrainedStringProperty() throws Exception {

        OpenAPI oas = TestUtils.deserializeJsonFileFromClasspath("specFiles/propertiesWithConstraints.json", OpenAPI.class);

        StringSchema property = (StringSchema) oas.getComponents().getSchemas().get("Health").getProperties().get("string_with_constraints");

        assertEquals(property.getMinLength(), Integer.valueOf(10));
        assertEquals(property.getMaxLength(), Integer.valueOf(100));
        assertEquals(property.getPattern(), "apattern");
    }

    @Test (description = "should deserialize an array property with constraints")
    public void testDeserializeConstrainedArrayProperties() throws Exception {

        OpenAPI oas = TestUtils.deserializeJsonFileFromClasspath("specFiles/propertiesWithConstraints.json", OpenAPI.class);

        Map<String, Schema> properties = oas.getComponents().getSchemas().get("Health").getProperties();

        ArraySchema withMin = (ArraySchema) properties.get("array_with_min");

        assertEquals(withMin.getMinItems(), Integer.valueOf(5));
        assertNull(withMin.getMaxItems());
        assertNull(withMin.getUniqueItems());

        ArraySchema withMax = (ArraySchema) properties.get("array_with_max");

        assertNull(withMax.getMinItems());
        assertEquals(withMax.getMaxItems(), Integer.valueOf(10));
        assertNull(withMax.getUniqueItems());

        ArraySchema withUnique = (ArraySchema) properties.get("array_with_unique");

        assertNull(withUnique.getMinItems());
        assertNull(withUnique.getMaxItems());
        assertEquals(withUnique.getUniqueItems(), Boolean.TRUE);

        ArraySchema withAll = (ArraySchema) properties.get("array_with_all");

        assertEquals(withAll.getMinItems(), Integer.valueOf(1));
        assertEquals(withAll.getMaxItems(), Integer.valueOf(10));
        assertEquals(withAll.getUniqueItems(), Boolean.TRUE);
    }

    @Test (description = "should deserialize a property with vendor extensions of different types")
    public void testDeserializePropertyWithVendorExtensions() throws Exception {

        OpenAPI oas = TestUtils.deserializeJsonFileFromClasspath("specFiles/propertyWithVendorExtensions.json", OpenAPI.class);

        Map<String, Object> oasVendorExtensions = oas.getExtensions();
        Map<String, Object> vendorExtensions = ((Schema)oas.getComponents().getSchemas().get("Health").getProperties().get("status")).getExtensions();

        assertVendorExtensions(oasVendorExtensions);
        assertVendorExtensions(vendorExtensions);

        //check for vendor extensions in array property types
        vendorExtensions = ((Schema)oas.getComponents().getSchemas().get("Health").getProperties().get("array")).getExtensions();

        String xStringValue = (String) vendorExtensions.get("x-string-value");
        assertNotNull(xStringValue);
        assertEquals(xStringValue, "string_value");
    }

    private void assertVendorExtensions(Map<String, Object> vendorExtensions) {
        assertNotNull(vendorExtensions);
        assertEquals(6, vendorExtensions.size());

        String xStringValue = (String) vendorExtensions.get("x-string-value");
        assertNotNull(xStringValue);
        assertEquals(xStringValue, "Hello World");

        assertTrue(vendorExtensions.containsKey("x-null-value"));
        assertNull(vendorExtensions.get("x-null-value"));

        Map<String, String> xMapValue = (Map) vendorExtensions.get("x-map-value");
        assertNotNull(xMapValue);
        assertEquals(xMapValue.get("hello"), "world");
        assertEquals(xMapValue.get("foo"), "bar");

        List<String> xListValue = (List) vendorExtensions.get("x-list-value");
        assertNotNull(xListValue);
        assertEquals(xListValue.get(0), "Hello");
        assertEquals(xListValue.get(1), "World");

        Integer xNumberValue = (Integer) vendorExtensions.get("x-number-value");
        assertNotNull(xNumberValue);
        assertEquals(xNumberValue.intValue(), 123);

        Boolean xBooleanValue = (Boolean) vendorExtensions.get("x-boolean-value");
        assertNotNull(xBooleanValue);
        assertTrue(xBooleanValue);

        assertFalse(vendorExtensions.containsKey("not-an-extension"));

    }

    @Test
    public void shouldDeserializeArrayPropertyMinItems() throws Exception {
        String path = "json-schema-validation/array.json";
        ArraySchema property = (ArraySchema)TestUtils.deserializeJsonFileFromClasspath(path, Schema.class);

        assertNotNull(property.getMinItems());
        assertEquals(property.getMinItems().intValue(), 1);
    }

    @Test
    public void shouldDeserializeArrayPropertyMaxItems() throws Exception {
        String path = "json-schema-validation/array.json";
        ArraySchema property = (ArraySchema)TestUtils.deserializeJsonFileFromClasspath(path, Schema.class);

        assertNotNull(property.getMaxItems());
        assertEquals(property.getMaxItems().intValue(), 10);
    }

    @Test
    public void shouldDeserializeArrayPropertyUniqueItems() throws Exception {
        String path = "json-schema-validation/array.json";
        ArraySchema property = (ArraySchema)TestUtils.deserializeJsonFileFromClasspath(path, Schema.class);

        assertNotNull(property.getUniqueItems());
        assertTrue(property.getUniqueItems());
    }

    @Test
    public void givenMapProperty_shouldDeserializeMinProperties() {
        String path = "json-schema-validation/map.json";
        MapSchema property = (MapSchema)TestUtils.deserializeJsonFileFromClasspath(path, Schema.class);

        assertNotNull(property.getMinProperties());
        assertEquals(property.getMinProperties().intValue(), 1);
    }

    @Test
    public void givenMapProperty_shouldDeserializeMaxProperties() {
        String path = "json-schema-validation/map.json";
        MapSchema property = (MapSchema)TestUtils.deserializeJsonFileFromClasspath(path, Schema.class);

        assertNotNull(property.getMaxProperties());
        assertEquals(property.getMaxProperties().intValue(), 10);
    }
}
