package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

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