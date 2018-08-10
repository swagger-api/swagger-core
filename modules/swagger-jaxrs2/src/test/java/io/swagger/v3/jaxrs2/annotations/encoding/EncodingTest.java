/*
* IBM Confidential
*
* OCO Source Materials
*
* WLP Copyright IBM Corp. 2017
*
* The source code for this program is not published or otherwise divested
* of its trade secrets, irrespective of what has been deposited with the
* U.S. Copyright Office.
*/
package io.swagger.v3.jaxrs2.annotations.encoding;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static org.testng.Assert.assertEquals;

public class EncodingTest extends AbstractAnnotationTest {
    //Test encoding inside ApiResponse/Content
    @Test
    public void testSimpleGetOperationWithEncodingInApiResponse() {

        String openApiYAML = readIntoYaml(SimpleGetOperationWithEncodingInApiResponse.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex\n" +
                "      operationId: simpleGet\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "              encoding:\n" +
                "                test:\n" +
                "                  contentType: text/plain\n" +
                "                  style: form\n" +
                "                  explode: true\n" +
                "                  allowReserved: true";

        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetOperationWithEncodingInApiResponse {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "simpleGet",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                name = "test"),
                                        encoding = @Encoding(
                                                name = "test",
                                                contentType = "text/plain",
                                                style = "FORM",
                                                allowReserved = true,
                                                explode = true)))
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    //Test encoding inside Parameter/Content
    @Test
    public void testGetOperationWithEncodingInParameter() {

        String openApiYAML = readIntoYaml(GetOperationWithEncodingInParameter.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex\n" +
                "      operationId: getWithEncodingInParameter\n" +
                "      parameters:\n" +
                "      - name: testParam\n" +
                "        in: query\n" +
                "        description: A parameter for testing encoding annotation.\n" +
                "        required: true\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: string\n" +
                "            encoding:\n" +
                "              testingParam:\n" +
                "                style: form\n" +
                "                allowReserved: true\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!";

        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class GetOperationWithEncodingInParameter {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithEncodingInParameter",
                parameters = {
                        @Parameter(
                                name = "testParam",
                                in = ParameterIn.QUERY,
                                description = "A parameter for testing encoding annotation.",
                                required = true,
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                name = "testingParam"),
                                        encoding = @Encoding(
                                                name = "testingParam",
                                                style = "FORM",
                                                allowReserved = true,
                                                explode = false))

                        )
                },
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    //Test encoding inside Parameter/Content
    @Test
    public void testGetOperationWithEncodingArrayInParameter() {

        String openApiYAML = readIntoYaml(GetOperationWithEncodingArrayInParameter.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex\n" +
                "      operationId: getWithEncodingArrayInParameter\n" +
                "      parameters:\n" +
                "      - name: testParam\n" +
                "        in: query\n" +
                "        description: A parameter for testing encoding annotation.\n" +
                "        required: true\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              title: testEncoding2\n" +
                "              type: string\n" +
                "            encoding:\n" +
                "              testEncoding:\n" +
                "                style: form\n" +
                "                allowReserved: true\n" +
                "              testEncoding2:\n" +
                "                headers:\n" +
                "                  testHeader:\n" +
                "                    required: true\n" +
                "                    style: simple\n" +
                "                style: form\n" +
                "                allowReserved: true\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!";

        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class GetOperationWithEncodingArrayInParameter {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithEncodingArrayInParameter",
                parameters = {
                        @Parameter(
                                name = "testParam",
                                in = ParameterIn.QUERY,
                                description = "A parameter for testing encoding annotation.",
                                required = true,
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                name = "testEncoding",
                                                title = "testEncoding2"),
                                        encoding = {
                                                @Encoding(
                                                        name = "testEncoding",
                                                        style = "FORM",
                                                        allowReserved = true,
                                                        explode = false),
                                                @Encoding(
                                                        name = "testEncoding2",
                                                        style = "FORM",
                                                        allowReserved = true,
                                                        explode = false,
                                                        headers = @Header(
                                                                name = "testHeader",
                                                                required = true))})

                        )
                },
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    //Test encoding inside RequestBody/Content
    @Test
    public void testGetOperationWithEncodingInRequestBody() {

        String openApiYAML = readIntoYaml(GetOperationWithEncodingInRequestBody.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with encoding in requestBody\n" +
                "      operationId: getWithEncodingInRequestBody\n" +
                "      requestBody:\n" +
                "        description: Test requestBody with encoding.\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: string\n" +
                "            encoding:\n" +
                "              testRequestBody:\n" +
                "                contentType: text/plain\n" +
                "                style: form\n" +
                "                explode: true\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class GetOperationWithEncodingInRequestBody {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with encoding in requestBody",
                operationId = "getWithEncodingInRequestBody",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                })

        @GET
        @Path("/path")
        public void simpleGet(@RequestBody(
                description = "Test requestBody with encoding.",
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                name = "testRequestBody"),
                        encoding = @Encoding(
                                name = "testRequestBody",
                                contentType = "text/plain",
                                style = "FORM",
                                explode = true))) String testRequestBody) {
        }
    }

    //Test an operation with ApiResponse but no Content/Encoding in it
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
                "          description: voila!";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetOperationTest {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    //Test an operation with no response, parameters, or requestBody
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
                "            '*/*': {}";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetWithoutResponses {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs or responses",
                operationId = "getWithNoParametersAndNoResponses")
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

}
