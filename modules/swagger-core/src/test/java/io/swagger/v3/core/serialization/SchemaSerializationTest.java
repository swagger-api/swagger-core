package io.swagger.v3.core.serialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.JsonSchema;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

public class SchemaSerializationTest {

    @Test
    public void serializeRefSchema3_1() {
        OpenAPI openAPI = new OpenAPI()
                .components(new Components()
                        .addSchemas("Pet", new JsonSchema()
                                .types(new HashSet<>(Arrays.asList("object")))
                                .format("whatever")
                                ._if(new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string"))))
                                .then(new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string"))))
                                ._else(new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string"))))
                                .minimum(BigDecimal.valueOf(1))
                                .addProperties("id", new JsonSchema().type("integer").types(new HashSet<>(Arrays.asList("integer"))))
                                .addProperties("name", new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string"))))
                                .addProperties("tag", new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string")))))
                        .addSchemas("AnotherPet", new JsonSchema()
                                .title("Another Pet")
                                .description("Another Pet for petstore referencing Pet schema")
                                .$ref("#/components/schemas/Pet")
                                .addProperties("category", new JsonSchema().types(new HashSet<>(Arrays.asList("string"))))
                                .addProperties("photoUrl", new JsonSchema().types(new HashSet<>(Arrays.asList("string"))))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, "openapi: 3.0.1\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      format: whatever\n" +
                "      if:\n" +
                "        type: string\n" +
                "      then:\n" +
                "        type: string\n" +
                "      else:\n" +
                "        type: string\n" +
                "      minimum: 1\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "        name:\n" +
                "          type: string\n" +
                "        tag:\n" +
                "          type: string\n" +
                "    AnotherPet:\n" +
                "      $ref: '#/components/schemas/Pet'\n" +
                "      description: Another Pet for petstore referencing Pet schema\n" +
                "      properties:\n" +
                "        category:\n" +
                "          type: string\n" +
                "        photoUrl:\n" +
                "          type: string\n" +
                "      title: Another Pet\n");
        SerializationMatchers.assertEqualsToYaml(openAPI, "openapi: 3.0.1\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Pet:\n" +
                "      minimum: 1\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "        name:\n" +
                "          type: string\n" +
                "        tag:\n" +
                "          type: string\n" +
                "      format: whatever\n" +
                "    AnotherPet:\n" +
                "      $ref: '#/components/schemas/Pet'\n");
    }
}
