package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.schemaResolution.SchemaResolutionResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class SchemaResolutionInlineTest {
    @Test
    public void testSchemaResolutionInline() {
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.INLINE));
        OpenAPI openAPI = reader.read(SchemaResolutionResource.class);
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
                "                type: object\n" +
                "                properties:\n" +
                "                  property1:\n" +
                "                    type: object\n" +
                "                    properties:\n" +
                "                      bar:\n" +
                "                        type: string\n" +
                "                    description: InlineSchemaFirst property 1\n" +
                "                    nullable: true\n" +
                "                    example: example\n" +
                "                  property2:\n" +
                "                    type: object\n" +
                "                    properties:\n" +
                "                      bar:\n" +
                "                        type: string\n" +
                "                    description: ' InlineSchemaFirst property 2'\n" +
                "                    example: example 2\n" +
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
                "                  type: object\n" +
                "                  properties:\n" +
                "                    bar:\n" +
                "                      type: object\n" +
                "                      properties:\n" +
                "                        property1:\n" +
                "                          type: object\n" +
                "                          properties:\n" +
                "                            bar:\n" +
                "                              type: string\n" +
                "                          description: property 1\n" +
                "                        property2:\n" +
                "                          type: object\n" +
                "                          properties:\n" +
                "                            bar:\n" +
                "                              type: string\n" +
                "                          description: property 2\n" +
                "                          example: example\n" +
                "                  description: InlineSchemaSecond property 1\n" +
                "                  nullable: true\n" +
                "                  example: examplesecond\n" +
                "                property2:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    bar:\n" +
                "                      type: string\n" +
                "                  description: InlineSchemaSecond property 2\n" +
                "                  example: InlineSchemaSecond example 2\n" +
                "              description: InlineSchemaSecond API\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                properties:\n" +
                "                  foo:\n" +
                "                    type: string\n" +
                "                  propertySecond1:\n" +
                "                    type: object\n" +
                "                    properties:\n" +
                "                      bar:\n" +
                "                        type: object\n" +
                "                        properties:\n" +
                "                          property1:\n" +
                "                            type: object\n" +
                "                            properties:\n" +
                "                              bar:\n" +
                "                                type: string\n" +
                "                            description: property 1\n" +
                "                          property2:\n" +
                "                            type: object\n" +
                "                            properties:\n" +
                "                              bar:\n" +
                "                                type: string\n" +
                "                            description: property 2\n" +
                "                            example: example\n" +
                "                    description: InlineSchemaSecond property 1\n" +
                "                    nullable: true\n" +
                "                    example: examplesecond\n" +
                "                  property2:\n" +
                "                    type: object\n" +
                "                    properties:\n" +
                "                      bar:\n" +
                "                        type: string\n" +
                "                    description: InlineSchemaSecond property 2\n" +
                "                    example: InlineSchemaSecond example 2\n" +
                "components:\n" +
                "  schemas:\n" +
                "    InlineSchemaFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: InlineSchemaFirst property 1\n" +
                "          nullable: true\n" +
                "          example: example\n" +
                "        property2:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: ' InlineSchemaFirst property 2'\n" +
                "          example: example 2\n" +
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
                "          type: object\n" +
                "          properties:\n" +
                "            property1:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                bar:\n" +
                "                  type: string\n" +
                "              description: property 1\n" +
                "            property2:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                bar:\n" +
                "                  type: string\n" +
                "              description: property 2\n" +
                "              example: example\n" +
                "      description: propertysecond\n" +
                "      nullable: true\n" +
                "      example: examplesecond\n" +
                "    InlineSchemaPropertySimple:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "      example: example\n" +
                "    InlineSchemaSecond:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n" +
                "        propertySecond1:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: object\n" +
                "              properties:\n" +
                "                property1:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    bar:\n" +
                "                      type: string\n" +
                "                  description: property 1\n" +
                "                property2:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    bar:\n" +
                "                      type: string\n" +
                "                  description: property 2\n" +
                "                  example: example\n" +
                "          description: InlineSchemaSecond property 1\n" +
                "          nullable: true\n" +
                "          example: examplesecond\n" +
                "        property2:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: InlineSchemaSecond property 2\n" +
                "          example: InlineSchemaSecond example 2\n" +
                "      description: InlineSchemaSecond API\n" +
                "    InlineSchemaSimple:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: property 1\n" +
                "        property2:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: property 2\n" +
                "          example: example\n\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }
}
