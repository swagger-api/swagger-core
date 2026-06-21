package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

public class DuplicatedOperationMethodNameResource {

    @GET
    @Path("/1")
    @Operation(operationId = "getSummaryAndDescription2",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription1() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/2")
    public Response getSummaryAndDescription2() {
        return Response.ok().entity("ok").build();
    }

    @POST
    @Path("/2")
    @Operation(operationId = "postSummaryAndDescription3",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response postSummaryAndDescription2() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/3")
    public Response getSummaryAndDescription3() {
        return Response.ok().entity("ok").build();
    }

    @POST
    @Path("/3")
    public Response postSummaryAndDescription3() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/4")
    public Response getSummaryAndDescription3(String foo) {
        return Response.ok().entity("ok").build();
    }

}
