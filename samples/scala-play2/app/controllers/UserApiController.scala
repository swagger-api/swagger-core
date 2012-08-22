package controllers

import models._
import api._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import play.api._
import play.api.mvc._
import play.api.data._
import play.data.validation._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.Play.current
import play.api.data.format.Formats._

import javax.ws.rs._
import java.io.StringWriter

@Api(value = "/user", description = "Operations about user")
object UserApiController extends BaseApiController {
  var userData = new UserData

  @ApiOperation(value = "Create user", notes = "This can only be done by the logged in user.")
  @ApiParamsImplicit(Array(
    new ApiParamImplicit(name = "body", value = "Created user object", required = true, dataType = "User", paramType = "body")))
  def createUser = Action { implicit request =>
    request.body.asJson match {
      case Some(e) => {
        val user = BaseApiController.mapper.readValue(e.toString, classOf[User]).asInstanceOf[User]
        userData.addUser(user)
        JsonResponse(user)
      }
      case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
    }
  }

  @Path("/createWithArray")
  @ApiOperation(value = "Creates list of users with given input array", responseClass = "void")
  @ApiParamsImplicit(Array(
    new ApiParamImplicit(name = "body", value = "List of user object", required = true, dataType = "Array[User]", paramType = "body")))
  def createUsersWithArrayInput = Action { implicit request =>
    request.body.asJson match {
      case Some(e) => {
        val users = BaseApiController.mapper.readValue(e.toString, classOf[Array[User]]).asInstanceOf[Array[User]]
        users.foreach(user => userData.addUser(user))
        JsonResponse(users)
      }
      case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
    }
  }

  @Path("/createWithList")
  @ApiOperation(value = "Creates list of users with given list input", responseClass = "void")
  @ApiParamsImplicit(Array(
    new ApiParamImplicit(name = "body", value = "List of user object", required = true, dataType = "List[User]", paramType = "body")))
  def createUsersWithListInput = Action { implicit request =>
    request.body.asJson match {
      case Some(e) => {
        val users = BaseApiController.mapper.readValue(e.toString, classOf[Array[User]]).asInstanceOf[Array[User]]
        users.foreach(user => userData.addUser(user))
        JsonResponse(users)
      }
      case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
    }
  }

  @Path("/{username}")
  @ApiOperation(value = "Updated user", notes = "This can only be done by the logged in user.")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid username supplied"),
    new ApiError(code = 404, reason = "User not found")))
  @ApiParamsImplicit(Array(
    new ApiParamImplicit(name = "username", value = "name that need to be updated", required = true, dataType = "String", paramType = "path"),
    new ApiParamImplicit(name = "body", value = "Updated user object", required = true, dataType = "User", paramType = "body")))
  def updateUser(username: String) = Action { implicit request =>
    request.body.asJson match {
      case Some(e) => {
        val user = BaseApiController.mapper.readValue(e.toString, classOf[User]).asInstanceOf[User]
        userData.addUser(user)
        JsonResponse(user)
      }
      case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
    }
  }

  @Path("/{username}")
  @ApiOperation(value = "Delete user", notes = "This can only be done by the logged in user.")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid username supplied"),
    new ApiError(code = 404, reason = "User not found")))
  def deleteUser(
    @ApiParam(value = "The name that needs to be deleted", required = true)@PathParam("username") username: String) = Action { implicit request =>
    userData.removeUser(username)
    Ok
  }

  @Path("/{username}")
  @ApiOperation(value = "Get user by user name", responseClass = "models.User")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid username supplied"),
    new ApiError(code = 404, reason = "User not found")))
  def getUserByName(
    @ApiParam(value = "The name that needs to be fetched. Use user1 for testing. ", required = true)@PathParam("username") username: String) = Action { implicit request =>
    userData.findUserByName(username) match {
      case Some(user) => JsonResponse(user)
      case None => JsonResponse(new value.ApiResponse(400, "Invalid input"))
    }
  }

  @Path("/login")
  @ApiOperation(value = "Logs user into the system", responseClass = "String")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid username and password combination")))
  def loginUser(
    @ApiParam(value = "The user name for login", required = true)@QueryParam("username") username: String,
    @ApiParam(value = "The password for login in clear text", required = true)@QueryParam("password") password: String) = Action { implicit request =>
    JsonResponse("logged in user session:" + System.currentTimeMillis())
  }

  @Path("/logout")
  @ApiOperation(value = "Logs out current logged in user session")
  def logoutUser() = Action { implicit request =>
    Ok
  }
}