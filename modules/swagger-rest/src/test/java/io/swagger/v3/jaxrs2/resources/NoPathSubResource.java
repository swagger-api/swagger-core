package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;

/**
 * The {@code NoPathSubResource} class defines test sub-resource without
 * {@link jakarta.ws.rs.Path} annotations.
 */
public class NoPathSubResource {

    @Operation(description = "Returns greeting")
    @GET
    public String getGreeting() {
        return "Hello!";
    }
}