package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.info.Info;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
	public Response getSummaryAndDescription(@QueryParam("subscriptionId") String subscriptionId,
											 @QueryParam("description") String description) {
		return Response.ok().entity("ok").build();
	}

}
