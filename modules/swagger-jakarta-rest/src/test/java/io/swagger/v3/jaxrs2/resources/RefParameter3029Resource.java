package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

public class RefParameter3029Resource {

    @GET
    @Path("/1")
    @Operation(
            summary = "Simple get operation",
            operationId = "sendPayload1",
            parameters = @Parameter(ref = "id"))
    public void sendPayload1() {
    }

    @GET
    @Path("/2")
    @Operation(
            summary = "Simple get operation",
            operationId = "sendPayload2")
    @Parameter(ref = "id")
    public void sendPayload2() {
    }

}
