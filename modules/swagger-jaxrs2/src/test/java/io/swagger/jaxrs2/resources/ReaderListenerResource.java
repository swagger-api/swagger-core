package io.swagger.jaxrs2.resources;

import io.swagger.jaxrs2.Reader;
import io.swagger.jaxrs2.ReaderListener;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public class ReaderListenerResource implements ReaderListener {

	@GET
	@Path("/")
	@Operation(operationId = "operationId",
			summary = "Operation Summary",
			description = "Operation Description")
	public Response getSummaryAndDescription(@QueryParam("subscriptionId") @Parameter(in = "path", name = "subscriptionId",
			required = true, description = "parameter description",
			allowEmptyValue = true, allowReserved = true,
			schema = @Schema(
					type = "string",
					format = "uuid",
					description = "the generated UUID",
					readOnly = true)
	) String subscriptionId,
											 @QueryParam("description") String description) {
		return Response.ok().entity("ok").build();
	}

	@Override
	public void beforeScan(Reader reader, OpenAPI openAPI) {
		openAPI.addTagsItem(new Tag().name("Tag-added-before-read"));
	}

	@Override
	public void afterScan(Reader reader, OpenAPI openAPI) {
		openAPI.addTagsItem(new Tag().name("Tag-added-after-read"));
	}
}
