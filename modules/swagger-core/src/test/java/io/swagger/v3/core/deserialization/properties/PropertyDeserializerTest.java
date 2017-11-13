package io.swagger.v3.core.deserialization.properties;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
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

        Schema property = Json.mapper().readValue(json, Schema.class);

        assertTrue(property instanceof IntegerSchema);
        IntegerSchema ip = (IntegerSchema) property;
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

        Schema param = Json.mapper().readValue(json, Schema.class);
        IntegerSchema ip = (IntegerSchema) param;
        assertEquals(ip.getMinimum(), new BigDecimal("32"));
        assertEquals(ip.getMaximum(), new BigDecimal("100"));

    }
}
