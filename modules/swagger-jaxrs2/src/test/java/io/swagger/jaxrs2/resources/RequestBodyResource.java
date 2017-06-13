package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.parameters.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by RafaelLopez on 5/20/17.
 */
public class RequestBodyResource {

	@GET
	@Path("/")
	@Operation(requestBody =
	@RequestBody(description = "Request description", content = @Content(
			mediaType = "application/json",
			schema = @Schema(name = "schema", implementation = ResponsesResource.SampleResponseSchema.class, example = "default")
	))
	)
	public void setRequestBody() {
	}
}
