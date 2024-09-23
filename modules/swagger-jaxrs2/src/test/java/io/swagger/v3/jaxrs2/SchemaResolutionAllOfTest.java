package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.schemaResolution.SchemaResolutionResourceSimple;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class SchemaResolutionAllOfTest {

    @Test
    public void testSchemaResolutionAllOf() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF));
        OpenAPI openAPI = reader.read(SchemaResolutionResourceSimple.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/inlineSchemaFirst:\n" +
                "    get:\n" +
                "      operationId: inlineSchemaFirst\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: InlineSchemaFirst Response API\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/InlineSchemaFirst'\n" +
                "  /test/inlineSchemaSecond:\n" +
                "    get:\n" +
                "      operationId: inlineSchemaFirst_1\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              allOf:\n" +
                "              - description: InlineSchemaSecond API\n" +
                "              - $ref: '#/components/schemas/InlineSchemaFirst'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    InlineSchemaFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          $ref: '#/components/schemas/InlineSchemaPropertyFirst'\n" +
                "    InlineSchemaPropertyFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "      nullable: true\n" +
                "      example: example\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }
}
