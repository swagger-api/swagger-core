package io.swagger.v3.core.deserialization.properties;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class MapPropertyDeserializerTest {
    private static final String json = "{\n" +
            "  \"tags\": [\n" +
            "    \"store\"\n" +
            "  ],\n" +
            "  \"summary\": \"Returns pet inventories by status\",\n" +
            "  \"description\": \"Returns a map of status codes to quantities\",\n" +
            "  \"operationId\": \"getInventory\",\n" +
            "  \"produces\": [\n" +
            "    \"application/json\"\n" +
            "  ],\n" +
            "  \"parameters\": [],\n" +
            "  \"responses\": {\n" +
            "    \"200\": {\n" +
            "      \"description\": \"successful operation\",\n" +
            "      \"content\": {\n" +
            "        \"*/*\": {\n" +
            "          \"schema\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"x-foo\": \"vendor x\",\n" +
            "            \"additionalProperties\": {\n" +
            "              \"type\": \"integer\",\n" +
            "              \"format\": \"int32\"\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"security\": [\n" +
            "    {\n" +
            "      \"api_key\": []\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private static final String jsonAdditionalPropertiesBoolean = "{\n" +
            "  \"tags\": [\n" +
            "    \"store\"\n" +
            "  ],\n" +
            "  \"summary\": \"Returns pet inventories by status\",\n" +
            "  \"description\": \"Returns a map of status codes to quantities\",\n" +
            "  \"operationId\": \"getInventory\",\n" +
            "  \"produces\": [\n" +
            "    \"application/json\"\n" +
            "  ],\n" +
            "  \"parameters\": [],\n" +
            "  \"responses\": {\n" +
            "    \"200\": {\n" +
            "      \"description\": \"successful operation\",\n" +
            "      \"content\": {\n" +
            "        \"*/*\": {\n" +
            "          \"schema\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"x-foo\": \"vendor x\",\n" +
            "            \"additionalProperties\": false\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"security\": [\n" +
            "    {\n" +
            "      \"api_key\": []\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test(description = "it should deserialize a response per #1349")
    public void testMapDeserialization() throws Exception {

        Operation operation = Json.mapper().readValue(json, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        Schema responseSchema = response.getContent().get("*/*").getSchema();
        assertNotNull(responseSchema);
        assertTrue(responseSchema instanceof MapSchema);

        MapSchema mp = (MapSchema) responseSchema;
        assertTrue(mp.getAdditionalProperties() instanceof IntegerSchema);
    }

    @Test(description = "it should deserialize a boolean additionalProperties")
    public void testBooleanAdditionalPropertiesDeserialization() throws Exception {

        Operation operation = Json.mapper().readValue(jsonAdditionalPropertiesBoolean, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        Schema responseSchema = response.getContent().get("*/*").getSchema();
        assertNotNull(responseSchema);
        assertTrue(responseSchema instanceof ObjectSchema);

        assertTrue(responseSchema.getAdditionalProperties() instanceof Boolean);
    }

    @Test(description = "it should serialize a boolean additionalProperties")
    public void testBooleanAdditionalPropertiesSerialization() throws Exception {

        Operation operation = Json.mapper().readValue(json, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        Schema responseSchema = response.getContent().get("*/*").getSchema();

        Schema schema = new ObjectSchema().additionalProperties(true);
        assertEquals(Json.pretty(schema), "{\n" +
                "  \"type\" : \"object\",\n" +
                "  \"additionalProperties\" : true\n" +
                "}");

        schema = new ObjectSchema().additionalProperties(responseSchema);
        assertEquals(Json.pretty(schema), "{\n" +
                "  \"type\" : \"object\",\n" +
                "  \"additionalProperties\" : {\n" +
                "    \"type\" : \"object\",\n" +
                "    \"additionalProperties\" : {\n" +
                "      \"type\" : \"integer\",\n" +
                "      \"format\" : \"int32\"\n" +
                "    },\n" +
                "    \"x-foo\" : \"vendor x\"\n" +
                "  }\n" +
                "}");
    }

    @Test(description = "vendor extensions should be included with object type")
    public void testMapDeserializationVendorExtensions() throws Exception {
        Operation operation = Json.mapper().readValue(json, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        Schema responseSchema = response.getContent().get("*/*").getSchema();
        assertNotNull(responseSchema);

        MapSchema mp = (MapSchema) responseSchema;
        assertTrue(mp.getExtensions().size() > 0);
        assertNotNull(mp.getExtensions().get("x-foo"));
        assertEquals(mp.getExtensions().get("x-foo"), "vendor x");
    }

    @Test(description = "it should read an example within an inlined schema")
    public void testIssue1261InlineSchemaExample() throws Exception {
        Operation operation = Yaml.mapper().readValue(
                "      responses:\n" +
                        "        200:\n" +
                        "          content:\n" +
                        "            '*/*':\n" +
                        "              description: OK\n" +
                        "              schema:\n" +
                        "                type: object\n" +
                        "                properties:\n" +
                        "                  id:\n" +
                        "                    type: integer\n" +
                        "                    format: int32\n" +
                        "                  name:\n" +
                        "                    type: string\n" +
                        "                required: [id, name]\n" +
                        "                example: ok", Operation.class);

        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);
        Schema schema = response.getContent().get("*/*").getSchema();
        Object example = schema.getExample();
        assertNotNull(example);
        assertTrue(example instanceof String);
        assertEquals(example, "ok");
    }
}