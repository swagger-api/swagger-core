package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;
import records.SchemaResolutionWithRecordSimpleResource;
import records.SchemaResolutionWithRecordsResource;

public class SchemaResolutionRecordsTest {

    @Test
    public void testSchemaResolutionInlineWithRecords(){
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.INLINE));
        OpenAPI openAPI = reader.read(SchemaResolutionWithRecordsResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/inlineSchemaFirst:
                    get:
                      operationId: inlineSchemaFirst
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: object
                                properties:
                                  property1:
                                    type: object
                                    properties:
                                      bar:
                                        type: string
                                    description: property
                                    example: example
                                  property2:
                                    type: object
                                    properties:
                                      bar:
                                        type: string
                                    description: property
                                    example: example
                  /test/inlineSchemaSecond:
                    get:
                      operationId: inlineSchemaSecond
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: object
                              properties:
                                foo:
                                  type: string
                                propertySecond1:
                                  type: object
                                  properties:
                                    bar:
                                      type: object
                                      properties:
                                        property1:
                                          type: object
                                          properties:
                                            bar:
                                              type: string
                                          description: property 1
                                        property2:
                                          type: object
                                          properties:
                                            bar:
                                              type: string
                                          description: property 2
                                          example: example
                                  description: InlineSchemaSecond property 1
                                  nullable: true
                                  example: exampleSecond
                                property2:
                                  type: object
                                  properties:
                                    bar:
                                      type: string
                                  description: InlineSchemaSecond property 2
                                  nullable: true
                                  example: example
                              description: InlineSchemaSecond API
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: object
                                properties:
                                  foo:
                                    type: string
                                  propertySecond1:
                                    type: object
                                    properties:
                                      bar:
                                        type: object
                                        properties:
                                          property1:
                                            type: object
                                            properties:
                                              bar:
                                                type: string
                                            description: property 1
                                          property2:
                                            type: object
                                            properties:
                                              bar:
                                                type: string
                                            description: property 2
                                            example: example
                                    description: InlineSchemaSecond property 1
                                    nullable: true
                                    example: exampleSecond
                                  property2:
                                    type: object
                                    properties:
                                      bar:
                                        type: string
                                    description: InlineSchemaSecond property 2
                                    nullable: true
                                    example: example
                components:
                  schemas:
                    InlineSchemaPropertyFirst:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                      nullable: true
                      example: example
                    InlineSchemaRecordFirst:
                      type: object
                      properties:
                        property1:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: property
                          example: example
                        property2:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: property
                          example: example
                    InlineSchemaPropertySecond:
                      type: object
                      properties:
                        bar:
                          type: object
                          properties:
                            property1:
                              type: object
                              properties:
                                bar:
                                  type: string
                              description: property 1
                            property2:
                              type: object
                              properties:
                                bar:
                                  type: string
                              description: property 2
                              example: example
                      description: propertySecond
                      nullable: true
                      example: exampleSecond
                    InlineSchemaPropertySimple:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                      example: example
                    InlineSchemaRecordSecond:
                      type: object
                      properties:
                        foo:
                          type: string
                        propertySecond1:
                          type: object
                          properties:
                            bar:
                              type: object
                              properties:
                                property1:
                                  type: object
                                  properties:
                                    bar:
                                      type: string
                                  description: property 1
                                property2:
                                  type: object
                                  properties:
                                    bar:
                                      type: string
                                  description: property 2
                                  example: example
                          description: InlineSchemaSecond property 1
                          nullable: true
                          example: exampleSecond
                        property2:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: InlineSchemaSecond property 2
                          nullable: true
                          example: example
                      description: InlineSchemaSecond API
                    InlineSchemaSimple:
                      type: object
                      properties:
                        property1:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: property 1
                        property2:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: property 2
                          example: example
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test
    public void testSchemaResolutionAllOfWithRecordTest(){
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF));
        OpenAPI openAPI = reader.read(SchemaResolutionWithRecordSimpleResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/inlineSchemaFirst:
                    get:
                      operationId: inlineSchemaFirst
                      responses:
                        default:
                          description: InlineSchemaFirst Response API
                          content:
                            '*/*':
                              schema:
                                $ref: '#/components/schemas/SchemaRecordFirst'
                  /test/inlineSchemaSecond:
                    get:
                      operationId: inlineSchemaFirst_1
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              allOf:
                              - description: InlineSchemaSecond API
                              - $ref: '#/components/schemas/SchemaRecordFirst'
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    InlineSchemaPropertyFirst:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                      nullable: true
                      example: example
                    SchemaRecordFirst:
                      type: object
                      properties:
                        property1:
                          $ref: '#/components/schemas/InlineSchemaPropertyFirst'
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }

    @Test
    public void testSchemaResolutionAllOfRefWithRecordsTest(){
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF_REF));
        OpenAPI openAPI = reader.read(SchemaResolutionWithRecordsResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/inlineSchemaFirst:
                    get:
                      operationId: inlineSchemaFirst
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: '#/components/schemas/InlineSchemaRecordFirst'
                  /test/inlineSchemaSecond:
                    get:
                      operationId: inlineSchemaSecond
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              description: InlineSchemaSecond API
                              allOf:
                              - $ref: '#/components/schemas/InlineSchemaRecordSecond'
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: '#/components/schemas/InlineSchemaRecordSecond'
                components:
                  schemas:
                    InlineSchemaPropertyFirst:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                      example: example
                    InlineSchemaRecordFirst:
                      type: object
                      properties:
                        property1:
                          $ref: '#/components/schemas/InlineSchemaPropertyFirst'
                        property2:
                          $ref: '#/components/schemas/InlineSchemaPropertyFirst'
                    InlineSchemaPropertySecond:
                      type: object
                      properties:
                        bar:
                          $ref: '#/components/schemas/InlineSchemaSimple'
                      description: propertySecond
                      example: exampleSecond
                    InlineSchemaPropertySimple:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                    InlineSchemaRecordSecond:
                      type: object
                      properties:
                        foo:
                          type: string
                        propertySecond1:
                          description: InlineSchemaSecond property 1
                          nullable: true
                          allOf:
                          - $ref: '#/components/schemas/InlineSchemaPropertySecond'
                        property2:
                          description: InlineSchemaSecond property 2
                          nullable: true
                          allOf:
                          - $ref: '#/components/schemas/InlineSchemaPropertyFirst'
                    InlineSchemaSimple:
                      type: object
                      properties:
                        property1:
                          description: property 1
                          allOf:
                          - $ref: '#/components/schemas/InlineSchemaPropertySimple'
                        property2:
                          description: property 2
                          example: example
                          allOf:
                          - $ref: '#/components/schemas/InlineSchemaPropertySimple'
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }
}
