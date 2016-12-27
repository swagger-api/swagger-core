package io.swagger.properties;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
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
      Response response = operation.getResponses().get("200");
      assertNotNull(response);
      
      Property responseSchema = response.getSchema();
      assertNotNull(responseSchema);
      assertTrue(responseSchema instanceof MapProperty);
      
      MapProperty mp = (MapProperty) responseSchema;
      assertTrue(mp.getAdditionalProperties() instanceof IntegerProperty);
  }

  @Test(description = "vendor extensions should be included with object type")
  public void testMapDeserializationVendorExtensions () throws Exception {
    Operation operation = Json.mapper().readValue(json, Operation.class);
    Response response = operation.getResponses().get("200");
    assertNotNull(response);

    Property responseSchema = response.getSchema();
    assertNotNull(responseSchema);

    MapProperty mp = (MapProperty) responseSchema;
    assertTrue(mp.getVendorExtensions().size() > 0);
    assertNotNull(mp.getVendorExtensions().get("x-foo"));
    assertEquals(mp.getVendorExtensions().get("x-foo"), "vendor x");
  }

    @Test(description = "it should read an example within an inlined schema")
    public void testIssue1261InlineSchemaExample() throws Exception {
        Operation operation = Yaml.mapper().readValue("      produces:\n" +
                "        - application/json\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          schema:\n" +
                "            type: object\n" +
                "            properties:\n" +
                "              id:\n" +
                "                type: integer\n" +
                "                format: int32\n" +
                "              name:\n" +
                "                type: string\n" +
                "            required: [id, name]\n" +
                "            example:\n" +
                "              id: 42\n" +
                "              name: Arthur Dent\n", Operation.class);

        Response response = operation.getResponses().get("200");
        assertNotNull(response);
        Property schema = response.getSchema();
        Object example = schema.getExample();
        assertNotNull(example);
        assertTrue(example instanceof ObjectNode);
        ObjectNode objectNode = (ObjectNode) example;
        assertEquals(objectNode.get("id").intValue(), 42);
        assertEquals(objectNode.get("name").textValue(), "Arthur Dent");
    }
}