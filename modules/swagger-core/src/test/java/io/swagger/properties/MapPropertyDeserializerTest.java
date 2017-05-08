package io.swagger.properties;

import io.swagger.oas.models.Operation;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.MapSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class MapPropertyDeserializerTest {
  private static final String json = "{" +
      "  \"tags\": [\"store\"]," +
      "  \"summary\": \"Returns pet inventories by status\"," +
      "  \"description\": \"Returns a map of status codes to quantities\"," +
      "  \"operationId\": \"getInventory\"," +
      "  \"produces\": [\"application/json\"]," +
      "  \"parameters\": []," +
      "  \"responses\": {" +
      "    \"200\": {" +
      "      \"description\": \"successful operation\"," +
      "      \"schema\": {" +
      "        \"type\": \"object\"," +
      "        \"x-foo\": \"vendor x\"," +
      "        \"additionalProperties\": {" +
      "          \"type\": \"integer\"," +
      "          \"format\": \"int32\"" +
      "        }" +
      "      }" +
      "    }" +
      "  }," +
      "  \"security\": [{" +
      "    \"api_key\": []" +
      "  }]" +
      "}";

  @Test(description = "it should deserialize a response per #1349")
  public void testMapDeserialization () throws Exception {

      Operation operation = Json.mapper().readValue(json, Operation.class);
      ApiResponse response = operation.getResponses().get("200");
      assertNotNull(response);
      
      Schema responseSchema = response.getContent().get("*/*").getSchema();
      assertNotNull(responseSchema);
      assertTrue(responseSchema instanceof MapSchema);
      
      MapSchema mp = (MapSchema) responseSchema;
      assertTrue(mp.getAdditionalProperties() instanceof IntegerSchema);
  }

  @Test(description = "vendor extensions should be included with object type")
  public void testMapDeserializationVendorExtensions () throws Exception {
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