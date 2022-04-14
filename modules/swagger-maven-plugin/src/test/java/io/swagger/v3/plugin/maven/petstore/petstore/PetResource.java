package io.swagger.v3.plugin.maven.petstore.petstore;

import io.swagger.v3.plugin.maven.resources.QueryResultBean;
import io.swagger.v3.plugin.maven.resources.data.PetData;
import io.swagger.v3.plugin.maven.resources.exception.NotFoundException;
import io.swagger.v3.plugin.maven.resources.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Consumes("application/json")
@Path("/pet")
@Produces({"application/json", "application/xml"})
public class PetResource {
    static PetData petData = new PetData();

    @GET
    @Path("/{petId}")
    @Operation(summary = "Find pet by ID",
            description = "Returns a pet when 0 < ID <= 10.  ID > 10 or nonintegers will simulate API error conditions",
            responses = {
                    @ApiResponse(
                            description = "The pet", content = @Content(
                            schema = @Schema(implementation = Pet.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404", description = "Pet not found")
            })
    public Response getPetById(
            @Parameter(description = "ID of pet that needs to be fetched"/*, _enum = "range[1,10]"*/, required = true)
            @PathParam("petId") final Long petId) throws NotFoundException {
        Pet pet = petData.getPetById(petId);
        if (null != pet) {
            return Response.ok().entity(pet).build();
        } else {
            throw new NotFoundException(404, "Pet not found");
        }
    }

    @POST
    @Consumes({"application/json", "application/xml"})
    @Operation(summary = "Add a new pet to the store",
            responses = {
                    @ApiResponse(responseCode = "405", description = "Invalid input")
            })
    public Response addPet(
            @Parameter(description = "Pet object that needs to be added to the store", required = true) final Pet pet) {
        petData.addPet(pet);
        return Response.ok().entity("SUCCESS").build();
    }

    @POST
    @Path("/bodynoannotation")
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    @Operation(summary = "Add a new pet to the store no annotation",
            responses = {
                    @ApiResponse(responseCode = "405", description = "Invalid input")
            })
    public Response addPetNoAnnotation(final Pet pet) {
        petData.addPet(pet);
        return Response.ok().entity("SUCCESS").build();
    }

    @POST
    @Path("/bodyid")
    @Consumes({"application/json", "application/xml"})
    @Operation(summary = "Add a new pet to the store passing an integer with generic parameter annotation",
            responses = {
                    @ApiResponse(responseCode = "405", description = "Invalid input")
            })
    public Response addPetByInteger(
            @Parameter(description = "Pet object that needs to be added to the store", required = true) final int petId) {
        return Response.ok().entity("SUCCESS").build();
    }

    @POST
    @Path("/bodyidnoannotation")
    @Consumes({"application/json", "application/xml"})
    @Operation(summary = "Add a new pet to the store passing an integer without parameter annotation",
            responses = {
                    @ApiResponse(responseCode = "405", description = "Invalid input")
            })
    public Response addPetByIntegerNoAnnotation(final int petId) {
        return Response.ok().entity("SUCCESS").build();
    }

    @PUT
    @Operation(summary = "Update an existing pet",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404", description = "Pet not found"),
                    @ApiResponse(responseCode = "405", description = "Validation exception")})
    public Response updatePet(
            @Parameter(description = "Pet object that needs to be added to the store", required = true) final Pet pet) {
        petData.addPet(pet);
        return Response.ok().entity("SUCCESS").build();
    }

    @GET
    @Path("/findByStatus")
    @Produces("application/xml")
    @Operation(summary = "Finds Pets by status",
            description = "Multiple status values can be provided with comma seperated strings",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Pet.class))),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid status value"
                    )}
    )
    public Response findPetsByStatus(
            @Parameter(description = "Status values that need to be considered for filter", required = true) @QueryParam("status") final String status,
            @BeanParam final QueryResultBean qr
    ) {
        return Response.ok(petData.findPetByStatus(status)).build();
    }

    @GET
    @Path("/findByTags")
    @Produces("application/json")
    @Operation(summary = "Finds Pets by tags",
            description = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
            responses = {
                    @ApiResponse(description = "Pets matching criteria",
                            content = @Content(schema = @Schema(implementation = Pet.class))
                    ),
                    @ApiResponse(description = "Invalid tag value", responseCode = "400")
            })
    @Deprecated
    public Response findPetsByTags(
            @Parameter(description = "Tags to filter by", required = true) @QueryParam("tags") final String tags) {
        return Response.ok(petData.findPetByTags(tags)).build();
    }
}
