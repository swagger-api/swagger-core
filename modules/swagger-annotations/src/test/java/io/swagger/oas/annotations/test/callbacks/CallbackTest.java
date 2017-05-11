package io.swagger.oas.annotations.test.callbacks;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.callbacks.Callback;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;
import io.swagger.oas.annotations.test.AbstractAnnotationTest;
import org.testng.annotations.Test;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class CallbackTest extends AbstractAnnotationTest {
    @Test(enabled = false)
    public void testSimpleCallback () {
        String yaml = readIntoYaml(SimpleCallback.class);

        assertEquals(yaml,
            "/test:\n" +
            "  post:\n" +
            "    description: subscribes a client to updates relevant to the requestor's account, as identified by the input token.  The supplied url will be used as the delivery address for response payloads\n" +
            "    \n" +
            "    operationId: subscribe\n" +
            "    parameters:\n" +
            "      - in: header\n" +
            "        name: x-auth-token\n" +
            "        description: the authentication token provided after initially authenticating to the application\n" +
            "        required: true\n" +
            "        schema:\n" +
            "          type: string\n" +
            "      - in: query\n" +
            "        name: url\n" +
            "        description: the URL to call with response data\n" +
            "        required: true\n" +
            "        schema:\n" +
            "          type: string\n" +
            "    responses:\n" +
            "      default:\n" +
            "        description: no description\n" +
            "          '*/*':\n" +
            "            schema:\n" +
            "              type: object\n" +
            "              properties:\n" +
            "                subscriptionId:\n" +
            "                  type: string\n" +
            "    callbacks:\n" +
            "      subscription:\n" +
            "        'http://$request.query.url':\n" +
            "          post:\n" +
            "            description: 'payload data will be sent'\n" +
            "            parameters:\n" +
            "              - in: path\n" +
            "                name: subscriptionId\n" +
            "                required: true\n" +
            "                schema:\n" +
            "                  description: the generated UUID\n" +
            "                  type: string\n" +
            "                  format: uuid\n" +
            "                  readOnly: true\n" +
            "            responses:\n" +
            "              200:\n" +
            "                description: Return this code if the callback was received and processed successfully\n" +
            "              205:\n" +
            "                description: Return this code to unsubscribe from future data updates\n" +
            "              default:\n" +
            "                description: All other response codes will disable this callback subscription");
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
                        @Parameter(in = "path", name = "subscriptionId", required = true, schema = @Schema(
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
        @Operation(description = "subscribes a client to updates relevant to the requestor's account, as " +
                "identified by the input token.  The supplied url will be used as the delivery address for response payloads")
        public SubscriptionResponse subscribe (@Schema(required = true, description = "the authentication token " +
                "provided after initially authenticating to the application") @HeaderParam("x-auth-token") String token,
                                               @Schema(required = true, description = "the URL to call with response " +
                                                       "data") @QueryParam("url") String url) {
            return null;
        }
    }

    static class SubscriptionResponse {
        private String subscriptionUuid;
    }
}
