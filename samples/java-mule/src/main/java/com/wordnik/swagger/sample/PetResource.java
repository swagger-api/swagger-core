package com.wordnik.swagger.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.Authorization;
import com.wordnik.swagger.annotations.AuthorizationScope;
import com.wordnik.swagger.jaxrs.PATCH;
import com.wordnik.swagger.sample.data.PetData;
import com.wordnik.swagger.sample.exception.NotFoundException;
import com.wordnik.swagger.sample.model.Pet;
import com.wordnik.swagger.sample.resource.JavaRestResourceUtil;

@Path("/pet")
@Api(value = "/pet", description = "Operations about pets", authorizations = {
  @Authorization(value = "petstore_auth", type = "oauth2",
  scopes = {
    @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
    @AuthorizationScope(scope = "read:pets", description = "read your pets")
  })
}, tags = "pet")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class PetResource {
  static PetData petData = new PetData();
  static JavaRestResourceUtil ru = new JavaRestResourceUtil();

  @GET
  @Path("/{petId}")
  @ApiOperation(value = "Find pet by ID", 
    notes = "Returns a pet when ID < 10.  ID > 10 or nonintegers will simulate API error conditions", 
    response = Pet.class,
    authorizations = @Authorization(value = "api_key", type = "api_key")
  )
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
      @ApiResponse(code = 404, message = "Pet not found") })
  public Response getPetById(
      @ApiParam(value = "ID of pet that needs to be fetched", allowableValues = "range[1,5]", required = true) @PathParam("petId") Long petId)
      throws NotFoundException {
    Pet pet = petData.getPetbyId(petId);
    if (null != pet) {
      return Response.ok().entity(pet).build();
    } else {
      throw new NotFoundException(404, "Pet not found");
    }
  }

  @PATCH
  @Path("/{petId}")
  @ApiOperation(value = "Deletes a pet")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid pet value")})
  public Response deletePet(
    @ApiParam() @HeaderParam("api_key") String apiKey,
    @ApiParam(value = "Pet id to delete", required = true)@PathParam("petId") Long petId) {
    petData.deletePet(petId);
    return Response.ok().build();
  }

  @POST
  @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @ApiOperation(value = "Add a new pet to the store")
  @ApiResponses(value = { @ApiResponse(code = 405, message = "Invalid input", response = com.wordnik.swagger.sample.model.ApiResponse.class) })
  public Response addPet(
      @ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet) {
    Pet updatedPet = petData.addPet(pet);
    return Response.ok().entity(updatedPet).build();
  }

  @PUT
  @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @ApiOperation(value = "Update an existing pet")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
      @ApiResponse(code = 404, message = "Pet not found"),
      @ApiResponse(code = 405, message = "Validation exception") })
  public Response updatePet(
      @ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet) {
    Pet updatedPet = petData.addPet(pet);
    return Response.ok().entity(updatedPet).build();
  }

  @GET
  @Path("/findByStatus")
  @ApiOperation(value = "Finds Pets by status", 
    notes = "Multiple status values can be provided with comma seperated strings", 
    response = Pet.class, 
    responseContainer = "List")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value") })
  public Response findPetsByStatus(
      @ApiParam(value = "Status values that need to be considered for filter", required = true, defaultValue = "available", allowableValues = "available,pending,sold", allowMultiple = true) @QueryParam("status") String status) {
    return Response.ok(petData.findPetByStatus(status)).build();
  }

  @GET
  @Path("/findByTags")
  @ApiOperation(value = "Finds Pets by tags",
    notes = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.", 
    response = Pet.class, 
    responseContainer = "List")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid tag value") })
  @Deprecated
  public Response findPetsByTags(
    @HeaderParam("api_key") String api_key,
      @ApiParam(value = "Tags to filter by", required = true, allowMultiple = true) @QueryParam("tags") String tags) {
    return Response.ok(petData.findPetByTags(tags)).build();
  }

  @POST
  @Path("/{petId}")
  @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
  @ApiOperation(value = "Updates a pet in the store with form data",
    consumes = MediaType.APPLICATION_FORM_URLENCODED)
  @ApiResponses(value = {
    @ApiResponse(code = 405, message = "Invalid input")})
  public Response  updatePetWithForm (
   @ApiParam(value = "ID of pet that needs to be updated", required = true)@PathParam("petId") String petId,
   @ApiParam(value = "Updated name of the pet", required = false)@FormParam("name") String name,
   @ApiParam(value = "Updated status of the pet", required = false)@FormParam("status") String status) {
    System.out.println(name);
    System.out.println(status);
    return Response.ok().entity(new com.wordnik.swagger.sample.model.ApiResponse(200, "SUCCESS")).build();
  }


}
