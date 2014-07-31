package controllers;

import java.io.IOException;

import api.*;

import com.wordnik.swagger.core.*;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;

import models.Pet;
import play.*;
import play.mvc.*;

import views.html.*;

@Api(value = "/pet", description = "Operations about pets")
public class PetApiController extends BaseApiController {
    static PetData petData = new PetData();

    @ApiOperation(nickname = "getPetById",value = "Find pet by ID",
            notes = "Returns a pet when ID < 10.  ID > 10 or nonintegers will simulate API error conditions",
            response = Pet.class, httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found")})
    public static Result getPetById(
            @ApiParam(value = "ID of pet that needs to be fetched", allowableValues = "range[1,5]", required = true) @PathParam("id") String petId) {
        return JsonResponse(petData.getPetbyId(Long.parseLong(petId)));
    }

    @ApiOperation(nickname = "addPet",value = "Add a new pet to the store", httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public static Result addPet() {
        Object o = request().body().asJson();
        try {
            Pet pet = (Pet) BaseApiController.mapper.readValue(o.toString(), Pet.class);
            petData.addPet(pet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse("SUCCESS");
    }

    @ApiOperation(nickname = "updatePet",value = "Update an existing pet", httpMethod = "PUT")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @ApiImplicitParams({@ApiImplicitParam(value = "Pet object that needs to be updated in the store", required = true, dataType = "Pet", paramType = "body")})
    public static Result updatePet() {
        Object o = request().body().asJson();
        try {
            Pet pet = (Pet) BaseApiController.mapper.readValue(o.toString(), Pet.class);
            petData.addPet(pet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse("SUCCESS");
    }

    @ApiOperation(nickname = "findPetByStatus", value = "Finds Pets by status",
            notes = "Multiple status values can be provided with comma seperated strings",
            response = Pet.class,
            responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid status value")})

    public static Result findPetsByStatus(
            @ApiParam(value = "Status values that need to be considered for filter", required = true, defaultValue = "available", allowableValues = "available,pending,sold", allowMultiple = true) @QueryParam("status") String status) {
        return JsonResponse(petData.findPetByStatus(status));
    }

    @ApiOperation(nickname = "findPetsByTags",value = "Finds Pets by tags",
            notes = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
            response = Pet.class,
            responseContainer = "List", httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid tag value")})
    @Deprecated
    public static Result findPetsByTags(
            @ApiParam(value = "Tags to filter by", required = true, allowMultiple = true) @QueryParam("tags") String tags) {
        return JsonResponse(petData.findPetByTags(tags));
    }
}