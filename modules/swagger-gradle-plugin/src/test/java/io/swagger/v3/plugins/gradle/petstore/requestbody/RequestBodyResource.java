package io.swagger.v3.plugins.gradle.petstore.requestbody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.plugins.gradle.resources.model.Pet;
import io.swagger.v3.plugins.gradle.resources.model.User;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

/**
 * Resource with RequestBody examples
 */
public class RequestBodyResource {
    @GET
    @Path("/methodWithRequestBodyWithoutContent")
    @Operation(summary = "Create user",
            description = "This can only be done by the logged in user.")
    public Response methodWithRequestBodyWithoutContent(
            @RequestBody(description = "Created user object", required = true) final User user) {
        return Response.ok().entity("").build();
    }

    @GET
    @Path("/methodWithRequestBodyWithoutContentWithoutImplementation")
    @Operation(summary = "Create user",
            description = "This can only be done by the logged in user.")
    public Response methodWithRequestBodyWithoutContentWithoutImplementation(
            @RequestBody(description = "Created user object", required = true,
                    content = @Content(
                            schema = @Schema(name = "User", description = "User description",
                                    example = "User Description", required = true))) final User user) {
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/methodWithRequestBodyAndTwoParameters")
    @Operation(summary = "Create user",
            description = "This can only be done by the logged in user.")
    public Response methodWithRequestBodyAndTwoParameters(
            @RequestBody(description = "Created user object", required = true,
                    content = @Content(
                            schema = @Schema(implementation = User.class))) final User user,
            @QueryParam("name") final String name, @QueryParam("code") final String code) {
        return Response.ok().entity("").build();
    }

    @PUT
    @Path("/methodWithRequestBodyWithoutAnnotation")
    @Operation(summary = "Modify user",
            description = "Modifying user.")
    public Response methodWithRequestBodyWithoutAnnotation(
            final User user) {
        return Response.ok().entity("").build();
    }

    @DELETE
    @Path("/methodWithoutRequestBodyAndTwoParameters")
    @Operation(summary = "Delete user",
            description = "This can only be done by the logged in user.")
    public Response methodWithoutRequestBodyAndTwoParameters(
            @QueryParam("name") final String name, @QueryParam("code") final String code) {
        return Response.ok().entity("").build();
    }

    @PUT
    @Path("/methodWithRequestBodyWithoutAnnotationAndTwoConsumes")
    @Operation(summary = "Modify pet",
            description = "Modifying pet.")
    @Consumes({"application/json", "application/xml"})
    public Response methodWithRequestBodyWithoutAnnotationAndTwoConsumes(
            final User user) {
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/methodWithTwoRequestBodyWithoutAnnotationAndTwoConsumes")
    @Operation(summary = "Create pet",
            description = "Creating pet.")
    @Consumes({"application/json", "application/xml"})
    public Response methodWithTwoRequestBodyWithoutAnnotationAndTwoConsumes(
            final Pet pet, final User user) {
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/methodWithTwoRequestBodyWithAnnotationAndTwoConsumes")
    @Operation(summary = "Create pet",
            description = "Creating pet.")
    @Consumes({"application/json", "application/xml"})
    public Response methodWithTwoRequestBodyWithAnnotationAndTwoConsumes(
            final @RequestBody(description = "Request Body Pet") Pet pet,
            @RequestBody(description = "Request Body User") final User user) {
        return Response.ok().entity("").build();
    }

    @DELETE
    @Path("/methodWithOneSimpleRequestBody")
    @Operation(summary = "Delete pet",
            description = "Deleting pet.")
    @Consumes({"application/json", "application/xml"})
    public Response methodWithOneSimpleRequestBody(final int id) {
        return Response.ok().entity("").build();
    }
}
