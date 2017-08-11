package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by RafaelLopez on 5/19/17.
 */
public class BasicFieldsResource {

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

}
