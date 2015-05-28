package resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import models.ModelContainingModelWithReference;
import models.ModelWithReference;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Api(value = "/basic")
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
    @ApiOperation(value = "Get Model", response= ModelContainingModelWithReference.class )
    public Response getModel() throws WebApplicationException {
        return Response.ok().build();
    }

    @GET
    @Path("/anotherModel")
    @ApiOperation(value = "Get Another Model", response= ModelWithReference.class )
    public Response getAnotherModel() throws WebApplicationException {
        return Response.ok().build();
    }
}
