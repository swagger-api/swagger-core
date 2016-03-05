package io.swagger.deserialization;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.swagger.models.Swagger;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;
import io.swagger.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class JsonDeserializationTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should deserialize the petstore")
    public void testPetstore() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore.json");
        final Object swagger = m.readValue(json, Swagger.class);
        assertTrue(swagger instanceof Swagger);
    }

    @Test(description = "it should deserialize the composition test")
    public void testCompositionTest() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/compositionTest.json");
        final Object swagger = m.readValue(json, Swagger.class);
        assertTrue(swagger instanceof Swagger);
    }

    @Test(description = "it should deserialize a simple ObjectProperty")
    public void testObjectProperty() throws IOException {
        final String json = "{\n" +
                "   \"type\":\"object\",\n" +
                "   \"title\":\"objectProperty\",\n" +
                "   \"description\":\"top level object\",\n" +
                "   \"properties\":{\n" +
                "      \"property1\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"First property\"\n" +
                "      },\n" +
                "      \"property2\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"Second property\"\n" +
                "      },\n" +
                "      \"property3\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"Third property\"\n" +
                "      }\n" +
                "   }\n" +
                "}";
        final Property result = m.readValue(json, Property.class);
        assertTrue(result instanceof ObjectProperty);
        assertEquals(3, ((ObjectProperty) result).getProperties().size());
        assertEquals("objectProperty", ((ObjectProperty) result).getTitle());

    }

    @Test(description = "it should deserialize nested ObjectProperty(s)")
    public void testNestedObjectProperty() throws IOException {
        final String json = "{\n" +
                "   \"type\":\"object\",\n" +
                "   \"description\":\"top level object\",\n" +
                "   \"properties\":{\n" +
                "      \"property1\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"First property\"\n" +
                "      },\n" +
                "      \"property2\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"Second property\"\n" +
                "      },\n" +
                "      \"property3\":{\n" +
                "         \"type\":\"object\",\n" +
                "         \"description\":\"Third property\",\n" +
                "         \"properties\":{\n" +
                "            \"property1\":{\n" +
                "               \"type\":\"string\",\n" +
                "               \"description\":\"First nested property\"\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}";
        final Property result = m.readValue(json, Property.class);
        assertTrue(result instanceof ObjectProperty);

        final Map<String, Property> firstLevelProperties = ((ObjectProperty) result).getProperties();
        assertEquals(firstLevelProperties.size(), 3);

        final Property property3 = firstLevelProperties.get("property3");
        assertTrue(property3 instanceof ObjectProperty);

        final Map<String, Property> secondLevelProperties = ((ObjectProperty) property3).getProperties();
        assertEquals(secondLevelProperties.size(), 1);
    }
}
