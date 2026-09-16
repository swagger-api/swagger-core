package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public class TestSubResource {
    @Path("/otherStatus")
    @GET
    @Operation(description = "Get the other status!")
    public String otherStatus() {
        return "{\"a\":\"Still Ok!\"}";
    }
}