package io.swagger.v3.jaxrs2.annotations.operations;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.GenericResponsesResource;
import io.swagger.v3.jaxrs2.resources.HiddenAnnotatedUserResource;
import io.swagger.v3.jaxrs2.resources.HiddenUserResource;
import io.swagger.v3.jaxrs2.resources.PetResource;
import io.swagger.v3.jaxrs2.resources.SimpleUserResource;
import io.swagger.v3.jaxrs2.resources.UserResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
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
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*': {}\n" +
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
                "              examples:\n" +
                "                boo:\n" +
                "                  summary: example of boo\n" +
                "                  description: boo\n" +
                "                  value: example\n" +
                "                  externalValue: example of external value\n" +
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
                                description = "boo",
                                content = @Content(
                                        mediaType = "*/*",
                                        schema = @Schema(implementation = GenericError.class),
                                        examples = {
                                                @ExampleObject(name = "boo", value = "example",
                                                        summary = "example of boo", externalValue = "example of external value")
                                        }

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

    @Test(description = "reads an operation with response examples defined")
    public void testOperationWithParameterExample() {
        String openApiYAML = readIntoYaml(GetOperationWithParameterExample.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with a parameter example\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      parameters:\n" +
                "      - in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "        example:\n" +
                "          id: 19877734\n" +
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

    static class GetOperationWithParameterExample {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with a parameter example",
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
        public void simpleGet(@Parameter(in = ParameterIn.QUERY, example = "{\"id\": 19877734}") String exParam) {
        }
    }

    static class GetOperationWithResponseSingleHeader {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        schema = @Schema(type = "integer"))})
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testOperationWithResponseSingleHeader() {
        String openApiYAML = readIntoYaml(GetOperationWithResponseSingleHeader.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          headers:\n" +
                "            Rate-Limit-Limit:\n" +
                "              description: The number of allowed requests in the current period\n" +
                "              style: simple\n" +
                "              schema:\n" +
                "                type: integer\n" +
                "      deprecated: true\n";
        assertEquals(expectedYAML, extractedYAML);
    }

    static class GetOperationWithResponseMultipleHeaders {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        schema = @Schema(type = "integer")),
                                        @Header(
                                                name = "X-Rate-Limit-Desc",
                                                description = "The description of rate limit",
                                                schema = @Schema(type = "string"))})
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testOperationWithResponseMultipleHeaders() {
        String openApiYAML = readIntoYaml(GetOperationWithResponseMultipleHeaders.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          headers:\n" +
                "            X-Rate-Limit-Desc:\n" +
                "              description: The description of rate limit\n" +
                "              style: simple\n" +
                "              schema:\n" +
                "                type: string\n" +
                "            Rate-Limit-Limit:\n" +
                "              description: The number of allowed requests in the current period\n" +
                "              style: simple\n" +
                "              schema:\n" +
                "                type: integer\n" +
                "      deprecated: true\n";
        assertEquals(expectedYAML, extractedYAML);
    }

    @Test(description = "reads the pet resource from sample")
    public void testCompletePetResource() throws IOException {
        String expectedYAML = "openapi: 3.0.1\n" +
                "paths:\n" +
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
                "  /pet/bodynoannotation:\n" +
                "    post:\n" +
                "      summary: Add a new pet to the store no annotation\n" +
                "      operationId: addPetNoAnnotation\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Pet'\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Pet'\n" +
                "      responses:\n" +
                "        405:\n" +
                "          description: Invalid input\n" +
                "  /pet/bodyid:\n" +
                "    post:\n" +
                "      summary: Add a new pet to the store passing an integer with generic parameter annotation\n" +
                "      operationId: addPetByInteger\n" +
                "      requestBody:\n" +
                "        description: Pet object that needs to be added to the store\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        405:\n" +
                "          description: Invalid input\n" +
                "  /pet/bodyidnoannotation:\n" +
                "    post:\n" +
                "      summary: Add a new pet to the store passing an integer without parameter annotation\n" +
                "      operationId: addPetByIntegerNoAnnotation\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              type: integer\n" +
                "              format: int32\n" +
                "      responses:\n" +
                "        405:\n" +
                "          description: Invalid input\n" +
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
                "      xml:\n" +
                "        name: Category\n" +
                "    Tag:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type: string\n" +
                "      xml:\n" +
                "        name: Tag\n" +
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
                "          xml:\n" +
                "            wrapped: true\n" +
                "          items:\n" +
                "            type: string\n" +
                "            xml:\n" +
                "              name: photoUrl\n" +
                "        tags:\n" +
                "          type: array\n" +
                "          xml:\n" +
                "            wrapped: true\n" +
                "          items:\n" +
                "            $ref: '#/components/schemas/Tag'\n" +
                "        status:\n" +
                "          type: string\n" +
                "          description: pet status in the store\n" +
                "          enum:\n" +
                "          - available,pending,sold\n" +
                "      xml:\n" +
                "        name: Pet";

        compareAsYaml(PetResource.class, expectedYAML);

    }

    @Test(description = "reads the resource with generic response from sample")
    public void testGenericResponseResource() throws IOException {
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Returns a list of somethings\n" +
                "      operationId: getSomethings\n" +
                "      responses:\n" +
                "        default:\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SomethingResponse'\n" +
                "  /overridden:\n" +
                "    get:\n" +
                "      summary: Returns a list of somethings\n" +
                "      operationId: getSomethingsOverridden\n" +
                "      responses:\n" +
                "        default:\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Something'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SomethingResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        data:\n" +
                "          $ref: '#/components/schemas/DataSomething'\n" +
                "    DataSomething:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        items:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: '#/components/schemas/Something'\n" +
                "    Data:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        items:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: object\n" +
                "    Something:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: string\n";
        compareAsYaml(GenericResponsesResource.class, yaml);
    }

    @Test(description = "reads the user resource from sample")
    public void testCompleteUserResource() throws IOException {
        String expectedYAML = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /user:\n" +
                "    post:\n" +
                "      summary: Create user\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: createUser\n" +
                "      requestBody:\n" +
                "        description: Created user object\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            'application/json': {}\n" +
                "            'application/xml': {}\n" +
                "  /user/createWithArray:\n" +
                "    post:\n" +
                "      summary: Creates list of users with given input array\n" +
                "      operationId: createUsersWithArrayInput\n" +
                "      requestBody:\n" +
                "        description: List of user object\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            'application/json': {}\n" +
                "            'application/xml': {}\n" +
                "  /user/createWithList:\n" +
                "    post:\n" +
                "      summary: Creates list of users with given input array\n" +
                "      operationId: createUsersWithListInput\n" +
                "      requestBody:\n" +
                "        description: List of user object\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: array\n" +
                "              items:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            'application/json': {}\n" +
                "            'application/xml': {}\n" +
                "  /user/{username}:\n" +
                "    get:\n" +
                "      summary: Get user by user name\n" +
                "      operationId: getUserByName\n" +
                "      parameters:\n" +
                "      - name: username\n" +
                "        in: path\n" +
                "        description: 'The name that needs to be fetched. Use user1 for testing. '\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: The user\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "        400:\n" +
                "          description: User not found\n" +
                "    put:\n" +
                "      summary: Updated user\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: updateUser\n" +
                "      parameters:\n" +
                "      - name: username\n" +
                "        in: path\n" +
                "        description: name that need to be deleted\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "        examples:\n" +
                "          example2:\n" +
                "            summary: Summary example 2\n" +
                "            description: example2\n" +
                "            value: example2\n" +
                "            externalValue: external value 2\n" +
                "          example1:\n" +
                "            summary: Summary example 1\n" +
                "            description: example1\n" +
                "            value: example1\n" +
                "            externalValue: external value 1\n" +
                "      requestBody:\n" +
                "        description: Updated user object\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: user updated\n" +
                "        400:\n" +
                "          description: Invalid user supplied\n" +
                "        404:\n" +
                "          description: User not found\n" +
                "    delete:\n" +
                "      summary: Delete user\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: deleteUser\n" +
                "      parameters:\n" +
                "      - name: username\n" +
                "        in: path\n" +
                "        description: The name that needs to be deleted\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: user deteled\n" +
                "        400:\n" +
                "          description: Invalid username supplied\n" +
                "        404:\n" +
                "          description: User not found\n" +
                "  /user/login:\n" +
                "    get:\n" +
                "      summary: Logs user into the system\n" +
                "      operationId: loginUser\n" +
                "      parameters:\n" +
                "      - name: username\n" +
                "        in: query\n" +
                "        description: The user name for login\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      - name: password\n" +
                "        in: query\n" +
                "        description: The password for login in clear text\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: Successfully logged in\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "        400:\n" +
                "          description: Invalid username/password supplied\n" +
                "  /user/logout:\n" +
                "    get:\n" +
                "      summary: Logs out current logged in user session\n" +
                "      operationId: logoutUser\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            'application/json': {}\n" +
                "            'application/xml': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        username:\n" +
                "          type: string\n" +
                "        firstName:\n" +
                "          type: string\n" +
                "        lastName:\n" +
                "          type: string\n" +
                "        email:\n" +
                "          type: string\n" +
                "        password:\n" +
                "          type: string\n" +
                "        phone:\n" +
                "          type: string\n" +
                "        userStatus:\n" +
                "          type: integer\n" +
                "          description: User Status\n" +
                "          format: int32\n" +
                "      xml:\n" +
                "        name: User";
        compareAsYaml(UserResource.class, expectedYAML);
    }

    @Test(description = "reads the simple user resource from sample")
    public void testSimpleUserResource() throws IOException {

        String expectedYAML = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /user:\n" +
                "    post:\n" +
                "      summary: Create user\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: createUser\n" +
                "      requestBody:\n" +
                "        description: Created user object\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            'application/json': {}\n" +
                "            'application/xml': {}\n" +
                "  /user/createUserWithReturnType:\n" +
                "    post:\n" +
                "      summary: Create user with return type\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: createUserWithReturnType\n" +
                "      requestBody:\n" +
                "        description: Created user object\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "  /user/createUserWithResponseAnnotation:\n" +
                "    post:\n" +
                "      summary: Create user with response annotation\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: createUserWithResponseAnnotation\n" +
                "      requestBody:\n" +
                "        description: Created user object\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: aaa\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "            application/xml:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/User'\n" +
                "  /user/createUserWithReturnTypeAndResponseAnnotation:\n" +
                "    post:\n" +
                "      summary: Create user with return type and response annotation\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: createUserWithReturnTypeAndResponseAnnotation\n" +
                "      requestBody:\n" +
                "        description: Created user object\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/User'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: aaa\n" +
                "components:\n" +
                "  schemas:\n" +
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        username:\n" +
                "          type: string\n" +
                "        firstName:\n" +
                "          type: string\n" +
                "        lastName:\n" +
                "          type: string\n" +
                "        email:\n" +
                "          type: string\n" +
                "        password:\n" +
                "          type: string\n" +
                "        phone:\n" +
                "          type: string\n" +
                "        userStatus:\n" +
                "          type: integer\n" +
                "          description: User Status\n" +
                "          format: int32\n" +
                "      xml:\n" +
                "        name: User";
        compareAsYaml(SimpleUserResource.class, expectedYAML);
    }

    @Test(description = "reads and skips the hidden user resource")
    public void testHiddenUserResource() {
        String openApiYAML = readIntoYaml(HiddenUserResource.class);
        assertEquals(openApiYAML, "openapi: 3.0.1\n");
    }

    @Test(description = "reads and skips the hidden user resource")
    public void testHiddenAnnotatedUserResource() throws IOException {
        String openApiYAML = readIntoYaml(HiddenAnnotatedUserResource.class);
        assertEquals(openApiYAML, "openapi: 3.0.1\n");
        compareAsYaml(HiddenAnnotatedUserResource.HiddenAnnotatedUserResourceMethodAndData.class, "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /user/2:\n" +
                "    post:\n" +
                "      summary: Create user\n" +
                "      description: This can only be done by the logged in user.\n" +
                "      operationId: createUserWithHiddenBeanProperty\n" +
                "      requestBody:\n" +
                "        description: Created user object\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/UserResourceBean'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            'application/json': {}\n" +
                "            'application/xml': {}\n" +
                "components:\n" +
                "  schemas:\n" +
                "    UserResourceBean:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        foo:\n" +
                "          type: string");

    }

    @Test
    public void testSimpleGetOperationWithSecurity() {

        String openApiYAML = readIntoYaml(SimpleGetOperationWithSecurity.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      security:\n" +
                "      - petstore-auth:\n" +
                "        - write:pets";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(expectedYAML, extractedYAML);
    }

    static class SimpleGetOperationWithSecurity {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                },
                security = @SecurityRequirement(
                        name = "petstore-auth",
                        scopes = "write:pets"))
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testSimpleGetOperationWithMultipleSecurity() {

        String openApiYAML = readIntoYaml(SimpleGetOperationWithMultipleSecurityScopes.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      security:\n" +
                "      - petstore-auth:\n" +
                "        - write:pets\n" +
                "        - read:pets";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetOperationWithMultipleSecurityScopes {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                },
                security = @SecurityRequirement(
                        name = "petstore-auth",
                        scopes = {"write:pets", "read:pets"}))
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testSimpleGetOperationWithMultipleSecurityAnnotations() {

        String openApiYAML = readIntoYaml(SimpleGetOperationWithMultipleSecurityAnnotations.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      security:\n" +
                "      - review-auth:\n" +
                "        - write:review\n" +
                "      - petstore-auth:\n" +
                "        - write:pets\n" +
                "        - read:pets\n" +
                "      - api_key: []";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetOperationWithMultipleSecurityAnnotations {
        @SecurityRequirement(name = "review-auth", scopes = {"write:review"})
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                },
                security = {@SecurityRequirement(
                        name = "petstore-auth",
                        scopes = {"write:pets", "read:pets"}),
                        @SecurityRequirement(
                                name = "api_key",
                                scopes = {}),})
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }
}
