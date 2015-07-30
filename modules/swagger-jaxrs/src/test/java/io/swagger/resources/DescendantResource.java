package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pet")
@Api(value = "/pet", authorizations = {
        @Authorization(value = "petstore_auth",
                scopes = {
                        @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
                        @AuthorizationScope(scope = "read:pets", description = "read your pets")
                })
}, tags = "pet")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DescendantResource extends AbstractResource<Long> implements InterfaceResource {

    @Override
    public Response overriddenMethodWithTypedParam(@ApiParam(value = "ID of pet to return child") Long petId) {
        return super.overriddenMethodWithTypedParam(petId);
    }

    @GET
    @Path("/{petId4}")
    @ApiOperation(value = "Find pet by ID",
            notes = "Returns a single pet",
            response = String.class,
            authorizations = @Authorization(value = "api_key")
    )
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found")})
    public Response methodWithoutTypedParam(@ApiParam(value = "ID of pet to return child") @PathParam("petId4") Long petId) {
        return Response.ok().build();
    }

    @Override
    public Response overriddenMethodWithoutTypedParam(@ApiParam(value = "ID of pet to return child") Long petId) {
        return super.overriddenMethodWithoutTypedParam(petId);
    }

    @Override
    public Response methodFromInterface(@ApiParam(value = "ID of pet to return") Number petId) {
        return null;
    }

    @Override
    public Response methodFromInterface(@ApiParam(value = "Method to check ArrayIndexOutOfBoundsException") @PathParam("petId6") Number petId, String str) {
        return null;
    }

    @Override
    public Response deprecatedMethodFromInterface(Number petId) {
        return null;
    }
}
