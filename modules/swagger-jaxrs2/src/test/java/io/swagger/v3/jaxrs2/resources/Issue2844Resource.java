package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Test resource for issue #2844: Unable to disable security for specific operations
 * when global security is defined.
 *
 * This resource demonstrates the issue where operations with empty security arrays
 * should disable security but currently don't generate the expected OpenAPI output.
 */

@SecurityRequirement(name = "security_key")
@Path("/auth")
public class Issue2844Resource {

    @GET
    @Path("/protected")
    @Operation(
        summary = "Protected endpoint",
        description = "This endpoint should inherit global security"
    )
    public Response getProtected() {
        return Response.ok("Protected content").build();
    }

    @POST
    @Path("/login")
    @SecurityRequirement
    @Operation(
        summary = "Login endpoint",
        description = "This endpoint should have NO security (empty array should disable security)"
    )
    public Response login() {
        return Response.ok("Login successful").build();
    }

    @GET
    @Path("/public")
    @Operation(
        summary = "Public endpoint",
        description = "Another endpoint that should have NO security (using empty security array)"
    )
    @SecurityRequirements
    public Response getPublic() {
        return Response.ok("Public content").build();
    }

    @POST
    @Path("/register")
    @Operation(
        summary = "Registration endpoint",
        description = "Registration should not require authentication",
        responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
        }
    )
    public Response register() {
        return Response.status(201).entity("User registered").build();
    }
}
