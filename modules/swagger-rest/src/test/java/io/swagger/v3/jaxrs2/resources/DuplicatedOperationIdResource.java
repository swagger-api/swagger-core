package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

public class DuplicatedOperationIdResource {

    @GET
    @Path("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription() {
        return Response.ok().entity("ok").build();
    }

    @POST
    @Path("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response postSummaryAndDescription() {
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
