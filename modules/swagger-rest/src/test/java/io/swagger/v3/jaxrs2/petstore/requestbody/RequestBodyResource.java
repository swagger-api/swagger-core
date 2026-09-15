package io.swagger.v3.jaxrs2.petstore.requestbody;

import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
