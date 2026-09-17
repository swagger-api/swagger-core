package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

public class SubResourceTail {

    @Operation(description = "Returns greeting")
    @GET
    @Path("/hello")
    public String getGreeting() {
        return "Hello!";
    }

    @Operation(description = "Echoes passed string")
    @GET
    @Path("{string}")
    public String getEcho(@PathParam("string") String string) {
        return string;
    }
}