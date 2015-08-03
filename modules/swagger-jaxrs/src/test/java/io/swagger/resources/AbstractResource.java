package io.swagger.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public abstract class AbstractResource<T extends Number> {
    private Logger LOGGER = LoggerFactory.getLogger(AbstractResource.class);

    @GET
    @Path("/{petId1}")
    @ApiOperation(value = "Find pet by ID",
            notes = "Returns a single pet",
            response = String.class,
            authorizations = @Authorization(value = "api_key")
    )
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found")})
    public Response overriddenMethodWithTypedParam(
            @ApiParam(value = "ID of pet to return") @PathParam("petId1") T petId) {
        String pet = "dog";
        return Response.ok().entity(pet).build();
    }

    @GET
    @Path("/{petId2}")
    @ApiOperation(value = "Find pet by ID",
            notes = "Returns a single pet",
            response = String.class,
            authorizations = @Authorization(value = "api_key")
    )
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found")})
    public Response methodWithoutTypedParam(
            @ApiParam(value = "ID of pet to return") @PathParam("petId2") Number petId) {
        String pet = "dog";
        return Response.ok().entity(pet).build();
    }

    @GET
    @Path("/{petId3}")
    @ApiOperation(value = "Find pet by ID",
            notes = "Returns a single pet",
            response = String.class,
            authorizations = @Authorization(value = "api_key")
    )
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found")})
    public Response overriddenMethodWithoutTypedParam(
            @ApiParam(value = "ID of pet to return") @PathParam("petId3") Long petId) {
        String pet = "dog";
        return Response.ok().entity(pet).build();
    }

    @DELETE
    @Path("/{petId1}")
    @ApiOperation(value = "Deletes a pet")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid pet value")})
    public Response deletePet(
            @ApiParam() @HeaderParam("api_key") String apiKey,
            @ApiParam(value = "Pet id to delete", required = true) @PathParam("petId1") Long petId) {
        return Response.ok().build();
    }
}
