package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.schemaResolution.SchemaResolutionAnnotatedResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

public class SchemaResolutionAnnotationTest {

    @Test
    public void testSchemaResolutionAnnotation() {
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()));
        OpenAPI openAPI = reader.read(SchemaResolutionAnnotatedResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/inlineSchemaFirst:\n" +
                "    get:\n" +
                "      operationId: inlineSchemaFirst\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/InlineSchemaFirst\"\n" +
                "  /test/inlineSchemaSecond:\n" +
                "    get:\n" +
                "      operationId: inlineSchemaSecond\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                foo:\n" +
                "                  type: string\n" +
                "                propertySecond1:\n" +
                "                  $ref: \"#/components/schemas/InlineSchemaPropertySecond\"\n" +
                "                property2:\n" +
                "                  $ref: \"#/components/schemas/InlineSchemaPropertyFirst\"\n" +
                "              description: InlineSchemaSecond API\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/InlineSchemaSecond\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    InlineSchemaFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          description: InlineSchemaFirst property 1\n" +
                "          nullable: true\n" +
                "          allOf:\n" +
                "          - $ref: \"#/components/schemas/InlineSchemaPropertyFirst\"\n" +
                "        property2:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: property\n" +
                "          example: example\n" +
                "    InlineSchemaPropertyFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "      example: example\n" +
                "    InlineSchemaPropertySecond:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          $ref: \"#/components/schemas/InlineSchemaSimple\"\n" +
                "      description: propertysecond\n" +
                "      nullable: true\n" +
                "      example: examplesecond\n" +
                "    InlineSchemaPropertySimple:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "    InlineSchemaSecond:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n" +
                "        propertySecond1:\n" +
                "          $ref: \"#/components/schemas/InlineSchemaPropertySecond\"\n" +
                "        property2:\n" +
                "          $ref: \"#/components/schemas/InlineSchemaPropertyFirst\"\n" +
                "      description: InlineSchemaSecond API\n" +
                "    InlineSchemaSimple:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: property\n" +
                "        property2:\n" +
                "          description: property 2\n" +
                "          example: example\n" +
                "          allOf:\n" +
                "          - $ref: \"#/components/schemas/InlineSchemaPropertySimple\"\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }
}
