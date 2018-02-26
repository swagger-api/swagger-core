package io.swagger.properties;

import io.swagger.models.properties.AbstractProperty;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
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


    @Test(description = "it should deserialize a property with xml")
    public void testXmlProperty() throws IOException {
        final String json = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"title\": \"root\",\n" +
                "  \"description\": \"root\",\n" +
                "  \"properties\": {\n" +
                "    \"progdoi\": {\n" +
                "      \"type\": \"array\",\n" +
                "      \"xml\": {\n" +
                "        \"wrapped\": true\n" +
                "      },\n" +
                "      \"items\": {\n" +
                "        \"type\": \"string\",\n" +
                "        \"xml\": {\n" +
                "          \"name\": \"doi\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"collectors\": {\n" +
                "      \"type\": \"array\",\n" +
                "      \"xml\": {\n" +
                "        \"wrapped\": true,\n" +
                "        \"name\": \"cols\"\n" +
                "      },\n" +
                "      \"items\": {\n" +
                "        \"$ref\": \"#/definitions/Actor\",\n" +
                "        \"xml\": {\n" +
                "          \"name\": \"collector\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        final Property result = Json.mapper().readValue(json, Property.class);
        assertTrue(result instanceof ObjectProperty);
        assertEquals(2, ((ObjectProperty) result).getProperties().size());
        assertTrue(((ObjectProperty) result).getProperties().get("progdoi").getXml().getWrapped());
        assertEquals(((ArrayProperty)((ObjectProperty) result).getProperties().get("progdoi")).getItems().getXml().getName(), "doi");
        assertTrue(((ObjectProperty) result).getProperties().get("collectors").getXml().getWrapped());
        assertEquals(((ObjectProperty) result).getProperties().get("collectors").getXml().getName(), "cols");
        assertEquals(((ArrayProperty)((ObjectProperty) result).getProperties().get("collectors")).getItems().getXml().getName(), "collector");
    }

    @DataProvider
    public Object[][] readOnlyDataProvider() {
        return new Object[][] {
            {"\"type\": \"integer\", \"format\": \"int32\""},
            {"\"type\": \"string\", \"format\": \"date\""},
            {"\"type\": \"number\""},
            {"\"type\": \"array\", \"items\": {\"type\": \"string\"}"},
            {"\"type\": \"object\", \"additionalProperties\": {\"type\": \"string\"}"},
            {"\"type\": \"object\", \"properties\": {\"prop1\": {\"type\": \"string\"}}"},
            {"\"$ref\": \"#/definitions/SomeObject\""}
        };
    }

    @Test(dataProvider = "readOnlyDataProvider")
    public void deserializePropertyWithReadOnlyValue(String type) throws Exception {
        String json =
            "{\n" +
                type + "," +
                "  \"readOnly\": true" +
                "}";

        Property param = Json.mapper().readValue(json, Property.class);
        AbstractProperty property = (AbstractProperty) param;
        assertTrue(property.getReadOnly());
    }
}
