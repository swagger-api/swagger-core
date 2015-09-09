package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;

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
  public void testMapDerserilization () throws Exception {

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
  public void testMapDeserilizationVendorExtensions () throws Exception {
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
}