package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.JsonIdentityCyclicResource;
import io.swagger.v3.jaxrs2.resources.JsonIdentityResource;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;

public class JsonIdentityTest {

    @Test(description = "Test JsonIdentity")
    public void testJsonIdentity() throws IOException {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(JsonIdentityResource.class);
        SerializationMatchers.assertEqualsToYaml(openAPI, EXPECTED_YAML);
    }

    @Test(description = "Test JsonIdentity Cyclic")
    public void testJsonIdentityCyclic() throws IOException {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(JsonIdentityCyclicResource.class);
        SerializationMatchers.assertEqualsToYaml(openAPI, EXPECTED_YAML_CYCLIC);
    }

    static final String EXPECTED_YAML_CYCLIC = "openapi: 3.0.1\n" +
            "paths:\n" +
            "  /pet:\n" +
            "    post:\n" +
            "      description: Add a single object\n" +
            "      operationId: test\n" +
            "      requestBody:\n" +
            "        content:\n" +
            "          '*/*':\n" +
            "            schema:\n" +
            "              $ref: '#/components/schemas/ModelWithJsonIdentityCyclic'\n" +
            "        required: true\n" +
            "      responses:\n" +
            "        default:\n" +
            "          description: default response\n" +
            "          content:\n" +
            "            application/json: {}\n" +
            "            application/xml: {}\n" +
            "components:\n" +
            "  schemas:\n" +
            "    SourceDefinition:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        driver:\n" +
            "          type: string\n" +
            "        name:\n" +
            "          type: string\n" +
            "        model:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "    ModelWithJsonIdentityCyclic:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        id:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "        sourceDefinitions:\n" +
            "          type: array\n" +
            "          items:\n" +
            "            $ref: '#/components/schemas/SourceDefinition'\n";

    static final String EXPECTED_YAML = "openapi: 3.0.1\n" +
            "paths:\n" +
            "  /pet:\n" +
            "    post:\n" +
            "      description: Add a single object\n" +
            "      operationId: test\n" +
            "      requestBody:\n" +
            "        content:\n" +
            "          '*/*':\n" +
            "            schema:\n" +
            "              $ref: '#/components/schemas/ModelWithJsonIdentity'\n" +
            "        required: true\n" +
            "      responses:\n" +
            "        default:\n" +
            "          description: default response\n" +
            "          content:\n" +
            "            application/json: {}\n" +
            "            application/xml: {}\n" +
            "components:\n" +
            "  schemas:\n" +
            "    SourceDefinition4:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        name:\n" +
            "          type: string\n" +
            "        testName2:\n" +
            "          type: integer\n" +
            "          format: int32\n" +
            "    SourceDefinition5:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        name:\n" +
            "          type: string\n" +
            "        '@id':\n" +
            "          type: integer\n" +
            "          format: int32\n" +
            "    ModelWithJsonIdentity:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        PropertyGeneratorAsId:\n" +
            "          type: string\n" +
            "        PropertyGeneratorAsProperty:\n" +
            "          $ref: '#/components/schemas/SourceDefinition1'\n" +
            "        ChangedPropertyName:\n" +
            "          type: string\n" +
            "        ChangedPropertyName2:\n" +
            "          type: string\n" +
            "        SourceWithoutPropertyAsId:\n" +
            "          type: string\n" +
            "        SourceWithoutPropertyAsProperty:\n" +
            "          $ref: '#/components/schemas/SourceDefinition3'\n" +
            "        IntSequenceGeneratorAsId:\n" +
            "          type: integer\n" +
            "          format: int32\n" +
            "        IntSequenceGeneratorAsProperty:\n" +
            "          $ref: '#/components/schemas/SourceDefinition4'\n" +
            "        IntSequenceWithoutPropertyAsId:\n" +
            "          type: integer\n" +
            "          format: int32\n" +
            "        IntSequenceWithoutPropertyAsProperty:\n" +
            "          $ref: '#/components/schemas/SourceDefinition5'\n" +
            "        UUIDGeneratorAsId:\n" +
            "          type: string\n" +
            "          format: uuid\n" +
            "        UUIDGeneratorAsProperty:\n" +
            "          $ref: '#/components/schemas/SourceDefinition6'\n" +
            "        UUIDGeneratorWithoutPropertyAsId:\n" +
            "          type: string\n" +
            "          format: uuid\n" +
            "        UUIDGeneratorWithoutPropertyAsProperty:\n" +
            "          $ref: '#/components/schemas/SourceDefinition7'\n" +
            "        GeneratorsNone:\n" +
            "          $ref: '#/components/schemas/SourceDefinition8'\n" +
            "        CustomGenerator:\n" +
            "          type: string\n" +
            "        WithoutJsonIdentityReference:\n" +
            "          $ref: '#/components/schemas/SourceDefinition10'\n" +
            "    SourceDefinition3:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        name:\n" +
            "          type: string\n" +
            "        driverId:\n" +
            "          type: string\n" +
            "        '@id':\n" +
            "          type: string\n" +
            "    SourceDefinition1:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        driver:\n" +
            "          type: string\n" +
            "        name:\n" +
            "          type: string\n" +
            "    SourceDefinition10:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        driver:\n" +
            "          type: string\n" +
            "        name:\n" +
            "          type: string\n" +
            "    SourceDefinition8:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        name:\n" +
            "          type: string\n" +
            "        driverId:\n" +
            "          type: string\n" +
            "    SourceDefinition6:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        name:\n" +
            "          type: string\n" +
            "        UUID2:\n" +
            "          type: string\n" +
            "          format: uuid\n" +
            "    SourceDefinition7:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        name:\n" +
            "          type: string\n" +
            "        '@id':\n" +
            "          type: string\n" +
            "          format: uuid\n";
}