package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.ModelContainingModelWithReference;
import io.swagger.models.ModelWithReference;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Api(value = "/basic")
@Path("/")
public class ResourceWithReferences {

    @GET
    @Path("/test")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Error", reference = "http://swagger.io/schemas.json#/Models/ErrorResponse")
    })
    public Response getTest() throws WebApplicationException {
        return Response.ok().build();
    }

    @GET
    @Path("/some")
    @ApiOperation(value = "Get Some", responseReference = "http://swagger.io/schemas.json#/Models/SomeResponse")
    public Response getSome() throws WebApplicationException {
        return Response.ok().build();
    }

    @GET
    @Path("/testSome")
    @ApiOperation(value = "Get Some", responseReference = "http://swagger.io/schemas.json#/Models/SomeResponse")
    @ApiResponses({
            @ApiResponse(code = 500, message = "Error", reference = "http://swagger.io/schemas.json#/Models/ErrorResponse")
    })
    public Response getTestSome() throws WebApplicationException {
        return Response.ok().build();
    }

    @GET
    @Path("/model")
    @ApiOperation(value = "Get Model", response = ModelContainingModelWithReference.class)
    public Response getModel() throws WebApplicationException {
        return Response.ok().build();
    }

    @GET
    @Path("/anotherModel")
    @ApiOperation(value = "Get Another Model", response = ModelWithReference.class)
    public Response getAnotherModel() throws WebApplicationException {
        return Response.ok().build();
    }
}
