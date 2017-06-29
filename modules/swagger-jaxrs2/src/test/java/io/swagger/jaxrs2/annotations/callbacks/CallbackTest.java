package io.swagger.jaxrs2.annotations.callbacks;

import io.swagger.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.callbacks.Callback;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

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
                "      description: \"subscribes a client to updates relevant to the requestor's account,\\\n" +
                "        \\ as identified by the input token.  The supplied url will be used as the\\\n" +
                "        \\ delivery address for response payloads\"\n" +
                "      operationId: \"subscribe\"\n" +
                "      parameters:\n" +
                "      - name: \"x-auth-token\"\n" +
                "        in: \"header\"\n" +
                "        schema:\n" +
                "          type: \"string\"\n" +
                "          description: \"the authentication token provided after initially authenticating\\\n" +
                "            \\ to the application\"\n" +
                "          readOnly: true\n" +
                "      - name: \"url\"\n" +
                "        in: \"query\"\n" +
                "        schema:\n" +
                "          type: \"string\"\n" +
                "          description: \"the URL to call with response data\"\n" +
                /*"          readOnly: true\n" +*/
                "      responses:\n" +
                "        default:\n" +
                "          description: \"no description\"\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: \"#/components/schemas/SubscriptionResponse\"\n" +
                "      callbacks:\n" +
                "        subscription:\n" +
                "          http://$request.query.url:\n" +
                "            post:\n" +
                "              description: \"payload data will be sent \"\n" +
                "              parameters:\n" +
                "              - name: \"subscriptionId\"\n" +
                "                in: \"path\"\n" +
                "                required: true\n" +
                "                schema:\n" +
                "                  type: \"string\"\n" +
                "                  description: \"the generated UUID\"\n" +
                "                  format: \"uuid\"\n" +
                "                  readOnly: true\n" +
                "              responses:\n" +
                "                200:\n" +
                "                  description: \"Return this code if the callback was received and\\\n" +
                "                    \\ processed successfully\"\n" +
                "                205:\n" +
                "                  description: \"Return this code to unsubscribe from future data updates\"\n" +
                "                default:\n" +
                "                  description: \"All other response codes will disable this callback\\\n" +
                "                    \\ subscription\"\n" +
                "components:\n" +
                "  schemas:\n" +
                "    string:\n" +
                "      type: \"string\"\n" +
                "      description: \"the generated UUID\"\n" +
                "      format: \"uuid\"\n" +
                "      readOnly: true\n" +
                "    SubscriptionResponse:\n" +
                "      type: \"object\"\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: \"string\"";
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
                        description = "payload data will be sent ",
                        parameters = {
                                @Parameter(in = "path", name = "subscriptionId", required = true,
                                        schema = @Schema(
                                                type = "string",
                                                format = "uuid",
                                                description = "the generated UUID",
                                                readOnly = true
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
                                description = "no description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = CallbackTest.SubscriptionResponse.class)
                        ))
                })
        public SubscriptionResponse subscribe(@Schema(readOnly = true, description = "the authentication token " +
                "provided after initially authenticating to the application", type = "string") @HeaderParam("x-auth-token") String token,
                                              @Schema(readOnly = true, description = "the URL to call with response " +
                                                      "data") @QueryParam("url") String url) {
            return null;
        }
    }

    static class SubscriptionResponse {
        public String subscriptionId;
    }
}
