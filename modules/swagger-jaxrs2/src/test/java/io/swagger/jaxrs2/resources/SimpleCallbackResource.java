package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.callbacks.Callback;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Created by RafaelLopez on 5/20/17.
 */

public class SimpleCallbackResource {
	@Path("/test")
	@POST
	@Callback(
			callbackUrlExpression = "http://$request.query.url",
			name = "subscription",
			operation = {
					@Operation(
							method = "post",
							description = "payload data will be sent",
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
							}),
					@Operation(
							method = "get",
							description = "payload data will be received"
							),
					@Operation(
							method = "put",
							description = "payload data will be sent"
							)})
	@Operation(description = "subscribes a client to updates relevant to the requestor's account, as " +
			"identified by the input token.  The supplied url will be used as the delivery address for response payloads")
	public SubscriptionResponse subscribe(@Schema(required = true, description = "the authentication token " +
			"provided after initially authenticating to the application") @HeaderParam("x-auth-token") String token,
										  @Schema(required = true, description = "the URL to call with response " +
												  "data") @QueryParam("url") String url) {
		return null;
	}

	static class SubscriptionResponse {
		private String subscriptionUuid;
	}
}

