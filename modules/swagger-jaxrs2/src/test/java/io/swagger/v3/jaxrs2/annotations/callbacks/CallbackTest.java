package io.swagger.v3.jaxrs2.annotations.callbacks;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.callbacks.Callbacks;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class CallbackTest extends AbstractAnnotationTest {
    @Test
    public void testSimpleCallback() {
        String openApiYAML = readIntoYaml(SimpleCallback.class);
        int start = openApiYAML.indexOf("/test:");
        int end = openApiYAML.length() - 1;
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = "/test:\n" +
                "    post:\n" +
                "      description: subscribes a client to updates relevant to the requestor's account,\n" +
                "        as identified by the input token.  The supplied url will be used as the delivery\n" +
                "        address for response payloads\n" +
                "      operationId: subscribe\n" +
                "      parameters:\n" +
                "      - name: x-auth-token\n" +
                "        in: header\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          description: the authentication token provided after initially authenticating\n" +
                "            to the application\n" +
                "          readOnly: true\n" +
                "      - name: url\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          description: the URL to call with response data\n" +
                "          readOnly: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: test description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      callbacks:\n" +
                "        subscription:\n" +
                "          http://$request.query.url:\n" +
                "            post:\n" +
                "              description: payload data will be sent\n" +
                "              parameters:\n" +
                "              - name: subscriptionId\n" +
                "                in: path\n" +
                "                required: true\n" +
                "                schema:\n" +
                "                  maximum: 34\n" +
                "                  minimum: 2\n" +
                "                  type: string\n" +
                "                  description: the generated UUID\n" +
                "                  format: uuid\n" +
                "                  readOnly: true\n" +
                "              responses:\n" +
                "                200:\n" +
                "                  description: Return this code if the callback was received and processed\n" +
                "                    successfully\n" +
                "                205:\n" +
                "                  description: Return this code to unsubscribe from future data updates\n" +
                "                default:\n" +
                "                  description: All other response codes will disable this callback\n" +
                "                    subscription\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SubscriptionResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: string";
        System.out.println(expectedYAML);
        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleCallback {
        @Path("/test")
        @POST
        @Callback(
                callbackUrlExpression = "http://$request.query.url",
                name = "subscription",
                operation =
                @Operation(
                        method = "post",
                        description = "payload data will be sent",
                        parameters = {
                                @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true,
                                        schema = @Schema(
                                                type = "string",
                                                format = "uuid",
                                                description = "the generated UUID",
                                                accessMode = Schema.AccessMode.READ_ONLY,
                                                minimum = "2",
                                                maximum = "34"
                                        ))
                        },
                        responses = {
                                @ApiResponse(
                                        responseCode = "200",
                                        description = "Return this code if the callback was received and processed successfully"
                                ),
                                @ApiResponse(
                                        responseCode = "205",
                                        description = "Return this code to unsubscribe from future data updates"
                                ),
                                @ApiResponse(
                                        responseCode = "default",
                                        description = "All other response codes will disable this callback subscription"
                                )
                        }))
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                responses = {
                        @ApiResponse(
                                responseCode = "default",
                                description = "test description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = CallbackTest.SubscriptionResponse.class)
                        ))
                })
        public SubscriptionResponse subscribe(@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "the authentication token " +
                "provided after initially authenticating to the application", type = "string") @HeaderParam("x-auth-token") String token,
                                              @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "the URL to call with response " +
                                                      "data") @QueryParam("url") String url) {
            return null;
        }
    }

    static class SubscriptionResponse {
        public String subscriptionId;
    }

    @Test
    public void simpleCallbacksWithOneCallbackTest() {
        String openApiYAML = readIntoYaml(SimpleCallbacksTest.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      callbacks:\n" +
                "        testCallback1:\n" +
                "          localhost:9080/airlines/reviews/{id}/1: {}";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(expectedYAML, extractedYAML);
    }

    static class SimpleCallbacksTest {
        @Callbacks({
                @Callback(name = "testCallback1", operation = @Operation(), callbackUrlExpression = "localhost:9080/airlines/reviews/{id}/1")
        })
        @Operation(
                summary = "Simple get operation",
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

    @Test
    public void simpleCallbacksWithOneCallbackWithOperationTest() {
        String openApiYAML = readIntoYaml(SimpleCallbacksTestWithOperation.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      callbacks:\n" +
                "        testCallback1:\n" +
                "          http://www.url.com:\n" +
                "            get:\n" +
                "              summary: get all the reviews\n" +
                "              operationId: getAllReviews\n" +
                "              responses:\n" +
                "                200:\n" +
                "                  description: successful operation\n" +
                "                  content:\n" +
                "                    application/json:\n" +
                "                      schema:\n" +
                "                        type: integer\n" +
                "                        format: int32";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(expectedYAML, extractedYAML);
    }

    static class SimpleCallbacksTestWithOperation {
        @Callbacks({
                @Callback(
                        name = "testCallback1",
                        operation = @Operation(
                                operationId = "getAllReviews",
                                summary = "get all the reviews",
                                method = "get",
                                responses = @ApiResponse(
                                        responseCode = "200",
                                        description = "successful operation",
                                        content = @Content(
                                                mediaType = "application/json",
                                                schema = @Schema(
                                                        type = "integer",
                                                        format = "int32")))),
                        callbackUrlExpression = "http://www.url.com")
        })
        @Operation(
                summary = "Simple get operation",
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

    @Test
    public void simpleCallbacksWithMultipleCallbackWithOperationTest() {
        String openApiYAML = readIntoYaml(MultipleCallbacksTestWithOperation.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      callbacks:\n" +
                "        testCallback1:\n" +
                "          http://www.url.com:\n" +
                "            get:\n" +
                "              summary: get all the reviews\n" +
                "              operationId: getAllReviews\n" +
                "              responses:\n" +
                "                200:\n" +
                "                  description: successful operation\n" +
                "                  content:\n" +
                "                    application/json:\n" +
                "                      schema:\n" +
                "                        type: integer\n" +
                "                        format: int32\n" +
                "        testCallback2:\n" +
                "          http://$request.query.url: {}";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(expectedYAML, extractedYAML);
    }

    static class MultipleCallbacksTestWithOperation {
        @Callbacks({
                @Callback(
                        name = "testCallback1",
                        operation = @Operation(
                                operationId = "getAllReviews",
                                summary = "get all the reviews",
                                method = "get",
                                responses = @ApiResponse(
                                        responseCode = "200",
                                        description = "successful operation",
                                        content = @Content(
                                                mediaType = "application/json",
                                                schema = @Schema(
                                                        type = "integer",
                                                        format = "int32")))),
                        callbackUrlExpression = "http://www.url.com"),
                @Callback(name = "testCallback2", operation = @Operation(), callbackUrlExpression = "http://$request.query.url")
        })
        @Operation(
                summary = "Simple get operation",
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

    @Test
    public void callbackCallbacksAnnotationTest() {
        String openApiYAML = readIntoYaml(CallbackCallbacksAnnotationTest.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      callbacks:\n" +
                "        testCallback:\n" +
                "          http://$requests.query.url: {}\n" +
                "        testCallback1:\n" +
                "          http://www.url.com:\n" +
                "            get:\n" +
                "              summary: get all the reviews\n" +
                "              operationId: getAllReviews\n" +
                "              responses:\n" +
                "                200:\n" +
                "                  description: successful operation\n" +
                "                  content:\n" +
                "                    application/json:\n" +
                "                      schema:\n" +
                "                        type: integer\n" +
                "                        format: int32\n" +
                "        testCallback2:\n" +
                "          http://$request.query.url: {}";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(expectedYAML, extractedYAML);
    }

    @Test
    public void repeatableCallbackCallbacksAnnotationTest() {
        String openApiYAML = readIntoYaml(RepeatableCallbackAnnotationTest.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = "get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      callbacks:\n" +
                "        testCallback:\n" +
                "          http://$requests.query.url: {}\n" +
                "        testCallback1:\n" +
                "          http://www.url.com:\n" +
                "            get:\n" +
                "              summary: get all the reviews\n" +
                "              operationId: getAllReviews\n" +
                "              responses:\n" +
                "                200:\n" +
                "                  description: successful operation\n" +
                "                  content:\n" +
                "                    application/json:\n" +
                "                      schema:\n" +
                "                        type: integer\n" +
                "                        format: int32\n" +
                "        testCallback2:\n" +
                "          http://$request.query.url: {}";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(expectedYAML, extractedYAML);
    }

    static class CallbackCallbacksAnnotationTest {
        @Callback(name = "testCallback", operation = @Operation(), callbackUrlExpression = "http://$requests.query.url")
        @Callbacks({
                @Callback(
                        name = "testCallback1",
                        operation = @Operation(
                                operationId = "getAllReviews",
                                summary = "get all the reviews",
                                method = "get",
                                responses = @ApiResponse(
                                        responseCode = "200",
                                        description = "successful operation",
                                        content = @Content(
                                                mediaType = "application/json",
                                                schema = @Schema(
                                                        type = "integer",
                                                        format = "int32")))),
                        callbackUrlExpression = "http://www.url.com"),
                @Callback(name = "testCallback2", operation = @Operation(), callbackUrlExpression = "http://$request.query.url")
        })
        @Operation(
                summary = "Simple get operation",
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

    static class RepeatableCallbackAnnotationTest {
        @Callback(name = "testCallback", operation = @Operation(), callbackUrlExpression = "http://$requests.query.url")
        @Callback(
                name = "testCallback1",
                operation = @Operation(
                        operationId = "getAllReviews",
                        summary = "get all the reviews",
                        method = "get",
                        responses = @ApiResponse(
                                responseCode = "200",
                                description = "successful operation",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(
                                                type = "integer",
                                                format = "int32")))),
                callbackUrlExpression = "http://www.url.com")
        @Callback(name = "testCallback2", operation = @Operation(), callbackUrlExpression = "http://$request.query.url")
        @Operation(
                summary = "Simple get operation",
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
}
