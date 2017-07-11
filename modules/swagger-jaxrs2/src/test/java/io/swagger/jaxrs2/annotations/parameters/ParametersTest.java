package io.swagger.jaxrs2.annotations.parameters;

import io.swagger.jaxrs2.annotations.callbacks.CallbackTest;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.callbacks.Callback;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Created by rafaellopez on 7/10/17.
 */
public class ParametersTest {
    static class SimpleOperations {
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
                                @Parameter(in = "path", name = "subscriptionId", required = true,
                                        schema = @Schema(
                                                type = "string",
                                                format = "uuid",
                                                description = "the generated UUID",
                                                readOnly = true
                                        ))
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
                                        implementation = ParametersTest.SubscriptionResponse.class)
                        ))
                })
        public ParametersTest.SubscriptionResponse subscribe(@Schema(readOnly = true, description = "the authentication token " +
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
