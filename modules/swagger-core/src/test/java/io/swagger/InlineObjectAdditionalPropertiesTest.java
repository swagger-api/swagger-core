package io.swagger;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Swagger;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.utils.PropertyModelConverter;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * An inline object schema declaring both {@code properties} and a boolean
 * {@code additionalProperties} must keep its properties and required list (#5401).
 */
public class InlineObjectAdditionalPropertiesTest {

    private static final String SPEC = "{\n" +
            "  \"swagger\": \"2.0\",\n" +
            "  \"info\": { \"title\": \"test\", \"version\": \"1.0.0\" },\n" +
            "  \"paths\": {},\n" +
            "  \"definitions\": {\n" +
            "    \"OrderResponse\": {\n" +
            "      \"type\": \"object\",\n" +
            "      \"required\": [\"result\"],\n" +
            "      \"properties\": {\n" +
            "        \"result\": {\n" +
            "          \"type\": \"object\",\n" +
            "          \"required\": [\"count\", \"items\"],\n" +
            "          \"properties\": {\n" +
            "            \"count\": { \"type\": \"integer\", \"format\": \"int32\" },\n" +
            "            \"items\": { \"type\": \"array\", \"items\": { \"$ref\": \"#/definitions/Order\" } }\n" +
            "          },\n" +
            "          \"additionalProperties\": false\n" +
            "        },\n" +
            "        \"open\": {\n" +
            "          \"type\": \"object\",\n" +
            "          \"properties\": { \"id\": { \"type\": \"string\" } },\n" +
            "          \"additionalProperties\": true\n" +
            "        },\n" +
            "        \"closedMap\": {\n" +
            "          \"type\": \"object\",\n" +
            "          \"additionalProperties\": false\n" +
            "        }\n" +
            "      },\n" +
            "      \"additionalProperties\": false\n" +
            "    },\n" +
            "    \"Order\": {\n" +
            "      \"type\": \"object\",\n" +
            "      \"properties\": { \"id\": { \"type\": \"string\" } }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private Property inlineProperty(String name) throws Exception {
        Swagger swagger = Json.mapper().readValue(SPEC, Swagger.class);
        Model model = swagger.getDefinitions().get("OrderResponse");
        return model.getProperties().get(name);
    }

    @Test
    public void keepsPropertiesAndRequiredWithAdditionalPropertiesFalse() throws Exception {
        Property result = inlineProperty("result");

        assertTrue(result instanceof ObjectProperty, "expected ObjectProperty but was " + result.getClass());
        ObjectProperty objectProperty = (ObjectProperty) result;
        assertEquals(objectProperty.getProperties().keySet(), new LinkedHashSet<>(Arrays.asList("count", "items")));
        assertEquals(objectProperty.getRequiredProperties(), Arrays.asList("count", "items"));
        assertNotNull(objectProperty.getAdditionalProperties());
        assertEquals(objectProperty.getAdditionalProperties().getBooleanValue(), Boolean.FALSE);
    }

    @Test
    public void serializesPropertiesRequiredAndAdditionalProperties() throws Exception {
        JsonNode node = Json.mapper().valueToTree(inlineProperty("result"));

        assertEquals(node.get("type").asText(), "object");
        assertTrue(node.get("properties").has("count"));
        assertTrue(node.get("properties").has("items"));
        assertEquals(node.get("required").size(), 2);
        assertTrue(node.get("additionalProperties").isBoolean());
        assertFalse(node.get("additionalProperties").asBoolean());
    }

    @Test
    public void keepsPropertiesWithAdditionalPropertiesTrue() throws Exception {
        Property open = inlineProperty("open");

        assertTrue(open instanceof ObjectProperty, "expected ObjectProperty but was " + open.getClass());
        ObjectProperty objectProperty = (ObjectProperty) open;
        assertTrue(objectProperty.getProperties().containsKey("id"));
        assertEquals(objectProperty.getAdditionalProperties().getBooleanValue(), Boolean.TRUE);
    }

    @Test
    public void objectWithoutPropertiesIsStillAMap() throws Exception {
        // behaviour introduced in 1.6.9 for maps must stay unchanged
        Property closedMap = inlineProperty("closedMap");

        assertTrue(closedMap instanceof MapProperty, "expected MapProperty but was " + closedMap.getClass());
        assertEquals(((MapProperty) closedMap).getAdditionalProperties().getBooleanValue(), Boolean.FALSE);
    }

    @Test
    public void converterCarriesAdditionalPropertiesToModel() throws Exception {
        Model model = new PropertyModelConverter().propertyToModel(inlineProperty("result"));

        assertTrue(model instanceof ModelImpl);
        ModelImpl modelImpl = (ModelImpl) model;
        assertTrue(modelImpl.getProperties().containsKey("count"));
        assertEquals(modelImpl.getAdditionalProperties().getBooleanValue(), Boolean.FALSE);
    }
}
