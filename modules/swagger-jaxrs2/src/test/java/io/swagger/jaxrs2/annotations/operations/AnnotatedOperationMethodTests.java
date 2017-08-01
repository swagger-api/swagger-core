package io.swagger.jaxrs2.annotations.operations;

import io.swagger.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.jaxrs2.resources.PetResource;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static org.testng.Assert.assertEquals;

public class AnnotatedOperationMethodTests extends AbstractAnnotationTest {
    @Test
    public void testSimpleGetOperation() {
        String openApiYAML = readIntoYaml(SimpleGetOperationTest.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      deprecated: true";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetOperationTest {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                }
        )
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testSimpleGetOperationWithoutResponses() {
        String openApiYAML = readIntoYaml(SimpleGetWithoutResponses.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs or responses\n" +
                "      operationId: getWithNoParametersAndNoResponses\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: no description\n" +
                "      deprecated: true";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetWithoutResponses {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs or responses",
                operationId = "getWithNoParametersAndNoResponses",
                deprecated = true
        )
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testGetOperationWithResponsePayloadAndAlternateCodes() {
        String openApiYAML = readIntoYaml(GetOperationWithResponsePayloadAndAlternateCodes.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/GenericError'\n" +
                "      deprecated: true\n";

        assertEquals(extractedYAML, expectedYAML);
    }

    static class GetOperationWithResponsePayloadAndAlternateCodes {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = SampleResponseSchema.class)
                                )
                        ),
                        @ApiResponse(
                                responseCode = "default",
                                description = "boo",
                                content = @Content(
                                        mediaType = "*/*",
                                        schema = @Schema(implementation = GenericError.class)
                                )
                        )
                }
        )
        @Path("/path")
        @GET
        public void simpleGet() {
        }
    }

    static class SampleResponseSchema {
        @Schema(description = "the user id")
        public String id;
    }

    static class GenericError {
        public int code;
        public String message;
    }

    @Test(description = "reads an operation with response examples defined")
    public void testOperationWithResponseExamples() {
        String openApiYAML = readIntoYaml(GetOperationWithResponseExamples.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "              examples:\n" +
                "                basic:\n" +
                "                  summary: shows a basic example\n" +
                "                  description: basic\n" +
                "                  value: '{id: 19877734}'\n" +
                "      deprecated: true\n";
        assertEquals(extractedYAML, expectedYAML);
    }

    static class GetOperationWithResponseExamples {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = SampleResponseSchema.class),
                                        examples = {
                                                @ExampleObject(
                                                        name = "basic",
                                                        summary = "shows a basic example",
                                                        value = "{id: 19877734}")
                                        }
                                )
                        )
                }
        )
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test(description = "reads an operation from sample")
    public void testCompleteOperation() {
        String openApiYAML = readIntoYaml(PetResource.class);
        int start = 0;
        int end = openApiYAML.length() - 1;
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = "openapi: 3.0.0-rc2\n" +
                "paths:\n" +
                "  /pet/{petId}:\n" +
                "    get:\n" +
                "      summary: Find pet by ID\n" +
                "      description: Returns a pet when 0 < ID <= 10.  ID > 10 or nonintegers will simulate API error conditions\n" +
                "      operationId: getPetById\n" +
                "      parameters:\n" +
                "      - name: petId\n" +
                "        in: path\n" +
                "        description: ID of pet that needs to be fetched\n" +
                "        required: true\n" +
                "        explode: false\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: The pet\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Pet'\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Pet'\n" +
                "        400:\n" +
                "          description: Invalid ID supplied\n" +
                "        404:\n" +
                "          description: Pet not found\n" +
                "  /pet:\n" +
                "    put:\n" +
                "      summary: Update an existing pet\n" +
                "      operationId: updatePet\n" +
                "      requestBody:\n" +
                "        description: Pet object that needs to be added to the store\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Pet'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        400:\n" +
                "          description: Invalid ID supplied\n" +
                "        404:\n" +
                "          description: Pet not found\n" +
                "        405:\n" +
                "          description: Validation exception\n" +
                "    post:\n" +
                "      summary: Add a new pet to the store\n" +
                "      operationId: addPet\n" +
                "      requestBody:\n" +
                "        description: Pet object that needs to be added to the store\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Pet'\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Pet'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        405:\n" +
                "          description: Invalid input\n" +
                "  /pet/findByStatus:\n" +
                "    get:\n" +
                "      summary: Finds Pets by status\n" +
                "      description: Multiple status values can be provided with comma seperated strings\n" +
                "      operationId: findPetsByStatus\n" +
                "      parameters:\n" +
                "      - name: status\n" +
                "        in: query\n" +
                "        description: Status values that need to be considered for filter\n" +
                "        required: true\n" +
                "        explode: false\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      - name: skip\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      - name: limit\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      responses:\n" +
                "        default:\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Pet'\n" +
                "        400:\n" +
                "          description: Invalid status value\n" +
                "  /pet/findByTags:\n" +
                "    get:\n" +
                "      summary: Finds Pets by tags\n" +
                "      description: Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.\n" +
                "      operationId: findPetsByTags\n" +
                "      parameters:\n" +
                "      - name: tags\n" +
                "        in: query\n" +
                "        description: Tags to filter by\n" +
                "        required: true\n" +
                "        explode: false\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: Pets matching criteria\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Pet'\n" +
                "        400:\n" +
                "          description: Invalid tag value\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Category:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type: string\n" +
                "    Tag:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type: string\n" +
                "    Pet:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        category:\n" +
                "          $ref: '#/components/schemas/Category'\n" +
                "        name:\n" +
                "          type: string\n" +
                "        photoUrls:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: string\n" +
                "        tags:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: '#/components/schemas/Tag'\n" +
                "        status:\n" +
                "          type: string\n" +
                "          description: pet status in the store\n" +
                "          enum:\n" +
                "          - available,pending,sold";
        assertEquals(extractedYAML, expectedYAML);
    }
}
