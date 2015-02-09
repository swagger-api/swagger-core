package resources;

import models.Pet;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

import java.util.*;

@Path("/pet")
@Api(value = "/pet", description = "Operations about pets")
@Produces({"application/json", "application/xml"})
public class PetResource {
  @POST
  @ApiOperation(value = "Add a single object")
  @ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
  public Response addSinglePet(
      @ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet) {
    return Response.ok().entity("SUCCESS").build();
  }

  @Path("/listOfObjects")
  @POST
  @ApiOperation(value = "Add a list of object")
  @ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
  public Response addListOfPets(
      @ApiParam(value = "Pets to add", required = true) List<Pet> pet) {
    return Response.ok().entity("SUCCESS").build();
  }

  @Path("/collectionOfObjects")
  @POST
  @ApiOperation(value = "Add a collection of object")
  @ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
  public Response addCollectionOfPets(
      @ApiParam(value = "Pets to add", required = true) Collection<Pet> pet) {
    return Response.ok().entity("SUCCESS").build();
  }

  @Path("/arrayOfObjects")
  @POST
  @ApiOperation(value = "Add an array of object")
  @ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input") })
  public Response addArrayOfPets(
      @ApiParam(value = "Pets to add", required = true) Pet[] pet) {
    return Response.ok().entity("SUCCESS").build();
  }
}
