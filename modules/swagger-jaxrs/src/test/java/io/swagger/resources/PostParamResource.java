package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.Pet;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

@Path("/pet")
@Api(value = "/pet", description = "Operations about pets")
@Produces({"application/json", "application/xml"})
public class PostParamResource {
    @Path("/singleObject")
    @POST
    @ApiOperation(value = "Add a single object")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response addSinglePet(
            @ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet) {
        return Response.ok().entity("SUCCESS").build();
    }

    @Path("/listOfObjects")
    @POST
    @ApiOperation(value = "Add a list of object")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response addListOfPets(
            @ApiParam(value = "Pets to add", required = true) List<Pet> pet) {
        return Response.ok().entity("SUCCESS").build();
    }

    @Path("/collectionOfObjects")
    @POST
    @ApiOperation(value = "Add a collection of object")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response addCollectionOfPets(
            @ApiParam(value = "Pets to add", required = true) Collection<Pet> pet) {
        return Response.ok().entity("SUCCESS").build();
    }

    @Path("/arrayOfObjects")
    @POST
    @ApiOperation(value = "Add an array of object")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response addArrayOfPets(
            @ApiParam(value = "Pets to add", required = true) Pet[] pet) {
        return Response.ok().entity("SUCCESS").build();
    }

    @Path("/singleString")
    @POST
    @ApiOperation(value = "Add a single string")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response addSingleString(
            @ApiParam(value = "String to add", required = true) String string) {
        return Response.ok().entity("SUCCESS").build();
    }

    @Path("/listOfStrings")
    @POST
    @ApiOperation(value = "Add a list of strings")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response addListOfStrings(
            @ApiParam(value = "Pets to add", required = true) List<String> strings) {
        return Response.ok().entity("SUCCESS").build();
    }

    @Path("/collectionOfStrings")
    @POST
    @ApiOperation(value = "Add a collection of strings")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response addCollectionOfStrings(
            @ApiParam(value = "Pets to add", required = true) Collection<String> strings) {
        return Response.ok().entity("SUCCESS").build();
    }

    @Path("/arrayOfStrings")
    @POST
    @ApiOperation(value = "Add an array of object")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public Response addArrayOfStrings(
            @ApiParam(value = "Strings to add", required = true) String[] strings) {
        return Response.ok().entity("SUCCESS").build();
    }
}
