package io.swagger.properties;

import io.swagger.oas.models.Operation;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ArrayPropertyDeserializerTest {
  private static final String yaml =
          "      operationId: something\n" +
          "      responses:\n" +
          "        200:\n" +
          "          content:\n" +
          "            '*/*':\n" +
          "              examples:\n" +
          "                simple:\n" +
          "                  value: Array example\n" +
          "                more:\n" +
          "                  value: with two items\n" +
          "              description: OK\n" +
          "              schema:\n" +
          "                type: array\n" +
          "                minItems: 3\n" +
          "                maxItems: 100\n" +
          "                items:\n" +
          "                  type: string\n";

  @Test(description = "it should includes the example in the arrayproperty")
  public void testArrayDeserialization () throws Exception {

      Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
      ApiResponse response = operation.getResponses().get("200");
      assertNotNull(response);

      MediaType media = response.getContent().get("*/*");
      Schema responseSchema = media.getSchema();
      assertTrue(media.getExamples().size() == 2);
      assertNotNull(responseSchema);
      assertTrue(responseSchema instanceof ArraySchema);

      ArraySchema mp = (ArraySchema) responseSchema;
      assertEquals(mp.getMinItems(), new Integer(3));
      assertEquals(mp.getMaxItems(), new Integer(100));
  }
}