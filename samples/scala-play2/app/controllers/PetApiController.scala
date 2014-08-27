package controllers

import models.Pet
import api._

import play.api.mvc._


import javax.ws.rs.{QueryParam, PathParam}


import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.ScalaJsonUtil

@Api(value = "/pet", description = "Operations about pets")
class PetApiController extends BaseApiController {
  var petData = new PetData

  def getOptions(path: String) = Action {
    implicit request => JsonResponse(new value.ApiResponse(200, "Ok"))
  }

  @ApiOperation(nickname = "getPetById", value = "Find pet by ID", notes = "Returns a pet", response = classOf[models.Pet], httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID supplied"),
    new ApiResponse(code = 404, message = "Pet not found")))
  def getPetById(
                  @ApiParam(value = "ID of the pet to fetch") @PathParam("id") id: String) = Action {
    implicit request =>
      petData.getPetbyId(getLong(0, 100000, 0, id)) match {
        case Some(pet) => JsonResponse(pet)
        case _ => JsonResponse(new value.ApiResponse(404, "Pet not found"), 404)
      }
  }

  @ApiOperation(nickname = "addPet",
    value = "Add a new Pet",
    response = classOf[Void],
    httpMethod = "POST",
    authorizations = Array(new Authorization(value="oauth2",
    scopes = Array(
      new AuthorizationScope(scope = "test:anything", description = "anything"),
      new AuthorizationScope(scope = "test:nothing", description = "nothing")
    ))))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Pet object that needs to be added to the store", required = true, dataType = "Pet", paramType = "body")))
  def addPet() = Action {
    implicit request =>
      request.body.asJson match {
        case Some(e) => {
          val pet = ScalaJsonUtil.mapper.readValue(e.toString, classOf[Pet]).asInstanceOf[Pet]
          petData.addPet(pet)
          Ok
        }
        case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
      }
  }

  @ApiOperation(nickname = "updatePet",
    value = "Update an existing Pet", response = classOf[Void], httpMethod = "PUT")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID supplied"),
    new ApiResponse(code = 404, message = "Pet not found"),
    new ApiResponse(code = 405, message = "Validation exception")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Pet object that needs to be updated in the store", required = true, dataType = "Pet", paramType = "body")))
  def updatePet() = Action {
    implicit request =>
      request.body.asJson match {
        case Some(e) => {
          val pet = ScalaJsonUtil.mapper.readValue(e.toString, classOf[Pet]).asInstanceOf[Pet]
          petData.addPet(pet)
          JsonResponse("SUCCESS")
        }
        case None => JsonResponse(new value.ApiResponse(404, "sorry"))
      }
  }

  @ApiOperation(nickname = "findPetByStatus",
    value = "Finds Pets by status",
    notes = "Multiple status values can be provided with comma seperated strings",
    response = classOf[models.Pet], responseContainer = "List", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid status value")))
  def findPetsByStatus(
                        @ApiParam(value = "Status values that need to be considered for filter", required = true, defaultValue = "available",
                          allowableValues = "available,pending,sold", allowMultiple = true) @QueryParam("status") status: String) = Action {
    implicit request =>
      var results = petData.findPetByStatus(status)
      JsonResponse(results)
  }

  @ApiOperation(nickname = "findPetsByTags",
    value = "Finds Pets by tags",
    notes = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
    response = classOf[models.Pet], responseContainer = "List", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid tag value")))
  def findPetsByTags(
                      @ApiParam(value = "Tags to filter by", required = true,
                        allowMultiple = true) @QueryParam("tags") tags: String) = Action {
    implicit request =>
      var results = petData.findPetByTags(tags)
      JsonResponse(results)
  }

  @ApiOperation(nickname = "attachImage",
    value = "Attach an Image File for a pet",
    notes = "Is not functional, only used to test file upload params",
    response = classOf[Void], httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid file format")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Image file to attach", required = true, dataType = "file", paramType = "body"),
    new ApiImplicitParam(name = "id", value = "ID of pet to which to attach image", required = true, dataType = "String", paramType = "path",
      allowableValues = "range[0,10]")))
  def attachImage(id: String) = Action {
    implicit request =>
      JsonResponse("SUCCESS")
  }
}

object PetApiController {}

