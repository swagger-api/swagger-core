package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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

    @GET
    @Path("/3")
    public Response getSummaryAndDescription3() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/4")
    public Response getSummaryAndDescription3(String foo) {
        return Response.ok().entity("ok").build();
    }

}
