package controllers

import models.Pet
import api._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.Play.current

import javax.ws.rs.{ QueryParam, PathParam }

import java.io.StringWriter

import play.api.data.format.Formats._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

@Api(value = "/pet", listingPath = "/api-docs.{format}/pet", description = "Operations about pets")
object PetApiController extends BaseApiController {
  var petData = new PetData

  def getOptions(path: String) = Action { implicit request => JsonResponse(new value.ApiResponse(200, "Ok")) }

  @ApiOperation(value = "Find pet by ID", notes = "Returns a pet", responseClass = "Pet", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 400, reason = "Invalid ID supplied"))),
    new ApiResponse(errors = Array(new ApiError(code = 404, reason = "Pet not found")))))
  def getPetById(
    @ApiParam(value = "ID of the pet to fetch")@PathParam("id") id: String) = Action { implicit request =>
    petData.getPetbyId(getLong(0, 100000, 0, id)) match {
      case Some(pet) => JsonResponse(pet)
      case _ => JsonResponse(new value.ApiResponse(404, "Pet not found"), 404)
    }
  }

  @ApiOperation(value = "Add a new Pet", responseClass = "void")
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 405, reason = "Invalid input")))))
  @ApiParamsImplicit(Array(
    new ApiParamImplicit(value = "Pet object that needs to be added to the store", required = true, dataType = "Pet", paramType = "body")))
  def addPet() = Action { implicit request =>
    request.body.asJson match {
      case Some(e) => {
        val pet = BaseApiController.mapper.readValue(e.toString, classOf[Pet]).asInstanceOf[Pet]
        petData.addPet(pet)
        Ok
      }
      case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
    }
  }

  @ApiOperation(value = "Update an existing Pet", responseClass = "void")
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 400, reason = "Invalid ID supplied"))),
    new ApiResponse(errors = Array(new ApiError(code = 404, reason = "Pet not found"))),
    new ApiResponse(errors = Array(new ApiError(code = 405, reason = "Validation exception")))))
  @ApiParamsImplicit(Array(
    new ApiParamImplicit(value = "Pet object that needs to be updated in the store", required = true, dataType = "Pet", paramType = "body")))
  def updatePet() = Action { implicit request =>
    request.body.asJson match {
      case Some(e) => {
        val pet = BaseApiController.mapper.readValue(e.toString, classOf[Pet]).asInstanceOf[Pet]
        petData.addPet(pet)
        JsonResponse("SUCCESS")
      }
      case None => JsonResponse(new value.ApiResponse(404, "sorry"))
    }
  }

  @ApiOperation(value = "Finds Pets by status",
    notes = "Multiple status values can be provided with comma seperated strings",
    responseClass = "models.Pet", multiValueResponse = true)
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 400, reason = "Invalid status value")))))
  def findPetsByStatus(
    @ApiParam(value = "Status values that need to be considered for filter", required = true, defaultValue = "available",
      allowableValues = "available,pending,sold", allowMultiple = true)@QueryParam("status") status: String) = Action { implicit request =>
    var results = petData.findPetByStatus(status)
    JsonResponse(results)
  }

  @ApiOperation(value = "Finds Pets by tags",
    notes = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
    responseClass = "models.Pet", multiValueResponse = true)
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 400, reason = "Invalid tag value")))))
  def findPetsByTags(
    @ApiParam(value = "Tags to filter by", required = true,
      allowMultiple = true)@QueryParam("tags") tags: String) = Action { implicit request =>
    var results = petData.findPetByTags(tags)
    JsonResponse(results)
  }

  @ApiOperation(value = "Attach an Image File for a pet",
    notes = "Is not functional, only used to test file upload params",
    responseClass = "void")
  @ApiResponses(Array(
    new ApiResponse(errors = Array(new ApiError(code = 400, reason = "Invalid file format")))))
  @ApiParamsImplicit(Array(
    new ApiParamImplicit(value = "Image file to attach", required = true, dataType = "file", paramType = "body"),
    new ApiParamImplicit(name = "id", value = "ID of pet to which to attach image", required = true, dataType = "String", paramType = "path",
      allowableValues = "range[0,10]")))
  def attachImage (id: String) = Action { implicit request =>
    JsonResponse("SUCCESS")
  }
}

