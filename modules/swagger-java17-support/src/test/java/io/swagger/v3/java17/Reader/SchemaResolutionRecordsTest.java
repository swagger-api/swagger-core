package io.swagger.v3.java17.Reader;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.java17.matchers.SerializationMatchers;
import io.swagger.v3.java17.resources.SchemaResolutionWithRecordSimpleResource;
import io.swagger.v3.java17.resources.SchemaResolutionWithRecordsResource;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class SchemaResolutionRecordsTest {

    @Test
    public void testSchemaResolutionInlineWithRecords(){
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.INLINE));
        OpenAPI openAPI = reader.read(SchemaResolutionWithRecordsResource.class);
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
                "                    description: property\n" +
                "                    example: example\n" +
                "                  property2:\n" +
                "                    type: object\n" +
                "                    properties:\n" +
                "                      bar:\n" +
                "                        type: string\n" +
                "                    description: property\n" +
                "                    example: example\n" +
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
                "                  example: exampleSecond\n" +
                "                property2:\n" +
                "                  type: object\n" +
                "                  properties:\n" +
                "                    bar:\n" +
                "                      type: string\n" +
                "                  description: InlineSchemaSecond property 2\n" +
                "                  nullable: true\n" +
                "                  example: example\n" +
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
                "                    example: exampleSecond\n" +
                "                  property2:\n" +
                "                    type: object\n" +
                "                    properties:\n" +
                "                      bar:\n" +
                "                        type: string\n" +
                "                    description: InlineSchemaSecond property 2\n" +
                "                    nullable: true\n" +
                "                    example: example\n" +
                "components:\n" +
                "  schemas:\n" +
                "    InlineSchemaPropertyFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "      nullable: true\n" +
                "      example: example\n" +
                "    InlineSchemaRecordFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: property\n" +
                "          example: example\n" +
                "        property2:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: property\n" +
                "          example: example\n" +
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
                "      description: propertySecond\n" +
                "      nullable: true\n" +
                "      example: exampleSecond\n" +
                "    InlineSchemaPropertySimple:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "      example: example\n" +
                "    InlineSchemaRecordSecond:\n" +
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
                "          example: exampleSecond\n" +
                "        property2:\n" +
                "          type: object\n" +
                "          properties:\n" +
                "            bar:\n" +
                "              type: string\n" +
                "          description: InlineSchemaSecond property 2\n" +
                "          nullable: true\n" +
                "          example: example\n" +
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
                "          example: example\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test
    public void testSchemaResolutionAllOfWithRecordTest(){
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF));
        OpenAPI openAPI = reader.read(SchemaResolutionWithRecordSimpleResource.class);
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
                "                $ref: '#/components/schemas/SchemaRecordFirst'\n" +
                "  /test/inlineSchemaSecond:\n" +
                "    get:\n" +
                "      operationId: inlineSchemaFirst_1\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              allOf:\n" +
                "              - description: InlineSchemaSecond API\n" +
                "              - $ref: '#/components/schemas/SchemaRecordFirst'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    InlineSchemaPropertyFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "      nullable: true\n" +
                "      example: example\n" +
                "    SchemaRecordFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          $ref: '#/components/schemas/InlineSchemaPropertyFirst'\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test
    public void testSchemaResolutionAllOfRefWithRecordsTest(){
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF_REF));
        OpenAPI openAPI = reader.read(SchemaResolutionWithRecordsResource.class);
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
                "                $ref: '#/components/schemas/InlineSchemaRecordFirst'\n" +
                "  /test/inlineSchemaSecond:\n" +
                "    get:\n" +
                "      operationId: inlineSchemaSecond\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              description: InlineSchemaSecond API\n" +
                "              allOf:\n" +
                "              - $ref: '#/components/schemas/InlineSchemaRecordSecond'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/InlineSchemaRecordSecond'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    InlineSchemaPropertyFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "      example: example\n" +
                "    InlineSchemaRecordFirst:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          $ref: '#/components/schemas/InlineSchemaPropertyFirst'\n" +
                "        property2:\n" +
                "          $ref: '#/components/schemas/InlineSchemaPropertyFirst'\n" +
                "    InlineSchemaPropertySecond:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          $ref: '#/components/schemas/InlineSchemaSimple'\n" +
                "      description: propertySecond\n" +
                "      example: exampleSecond\n" +
                "    InlineSchemaPropertySimple:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        bar:\n" +
                "          type: string\n" +
                "      description: property\n" +
                "    InlineSchemaRecordSecond:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string\n" +
                "        propertySecond1:\n" +
                "          description: InlineSchemaSecond property 1\n" +
                "          nullable: true\n" +
                "          allOf:\n" +
                "          - $ref: '#/components/schemas/InlineSchemaPropertySecond'\n" +
                "        property2:\n" +
                "          description: InlineSchemaSecond property 2\n" +
                "          nullable: true\n" +
                "          allOf:\n" +
                "          - $ref: '#/components/schemas/InlineSchemaPropertyFirst'\n" +
                "    InlineSchemaSimple:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        property1:\n" +
                "          description: property 1\n" +
                "          allOf:\n" +
                "          - $ref: '#/components/schemas/InlineSchemaPropertySimple'\n" +
                "        property2:\n" +
                "          description: property 2\n" +
                "          example: example\n" +
                "          allOf:\n" +
                "          - $ref: '#/components/schemas/InlineSchemaPropertySimple'\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }
}
