package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class DuplicatedOperationIdResource {

    @GET
    @Path("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/path")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getDuplicatedOperation() {
        return Response.ok().entity("ok").build();
    }

}
