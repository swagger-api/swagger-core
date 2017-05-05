package io.swagger.properties;

import io.swagger.models.Operation;
import io.swagger.models.media.ArraySchema;
import io.swagger.models.media.Schema;
import io.swagger.models.responses.Response;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ArrayPropertyDeserializerTest {
  private static final String yaml = "      produces:\n" +
          "        - application/yaml\n" +
          "      responses:\n" +
          "        200:\n" +
          "          description: OK\n" +
          "          schema:\n" +
          "            type: array\n" +
          "            minItems: 3\n" +
          "            maxItems: 100\n" +
          "            items:\n" +
          "              type: string\n" +
          "            example:\n" +
          "              - Array example\n" +
          "              - with two items";

  @Test(description = "it should includes the example in the arrayproperty")
  public void testArrayDeserialization () throws Exception {

      Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
      Response response = operation.getResponses().get("200");
      assertNotNull(response);
      
      Schema responseSchema = response.getContent().get("*/*").getSchema();
      assertNotNull(responseSchema);
      assertTrue(responseSchema instanceof ArraySchema);
      
      ArraySchema mp = (ArraySchema) responseSchema;
      assertNotNull( mp.getExample() );
      assertEquals(mp.getMinItems(), new Integer(3));
      assertEquals(mp.getMaxItems(), new Integer(100));
  }
}