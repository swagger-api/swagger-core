package io.swagger.properties;

import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PropertyDeserializerTest {
    @Test
    public void deserializeParameterWithMinimumMaximumValues() throws Exception {
        String json =
            "{\n" +
            "  \"in\": \"query\",\n" +
            "  \"type\": \"integer\",\n" +
            "  \"format\": \"int32\",\n" +
            "  \"minimum\": 32,\n" +
            "  \"maximum\": 100\n" +
            "}";

        Property property = Json.mapper().readValue(json, Property.class);

        assertTrue(property instanceof IntegerProperty);
        IntegerProperty ip = (IntegerProperty) property;
        assertEquals(ip.getMinimum(), new BigDecimal("32"));
        assertEquals(ip.getMaximum(), new BigDecimal("100"));
    }

    @Test
    public void deserializePropertyWithMinimumMaximumValues() throws Exception {
        String json =
            "{\n" +
            "  \"type\": \"integer\",\n" +
            "  \"format\": \"int32\",\n" +
            "  \"minimum\": 32,\n" +
            "  \"maximum\": 100\n" +
            "}";

        Property param = Json.mapper().readValue(json, Property.class);
        IntegerProperty ip = (IntegerProperty) param;
        assertEquals(ip.getMinimum(), new BigDecimal("32"));
        assertEquals(ip.getMaximum(), new BigDecimal("100"));

    }
}
