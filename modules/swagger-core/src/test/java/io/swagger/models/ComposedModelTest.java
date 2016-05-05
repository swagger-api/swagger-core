package io.swagger.models;

import io.swagger.util.Json;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ComposedModelTest {
    @Test
    public void testDeserializeComposedModel() throws Exception {
        String json = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"allOf\": [\n" +
                "    {\n" +
                "      \"$ref\": \"#/definitions/Pet\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"$ref\": \"#/definitions/Furry\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"propInt\": {\n" +
                "          \"type\": \"string\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"propEx\": {\n" +
                "          \"type\": \"string\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Model model = Json.mapper().readValue(json, Model.class);
        assertTrue(model instanceof ComposedModel);
        ComposedModel cm = (ComposedModel) model;

        assertTrue(cm.getAllOf().size() == 4);
        assertTrue(cm.getAllOf().get(2).getProperties().containsKey("propInt"));
        assertTrue(cm.getAllOf().get(3).getProperties().containsKey("propEx"));

        assertNull(cm.getProperties());
    }
}
