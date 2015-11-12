package test.testdata


import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException

import play.api.mvc.{Action, Controller}
import play.Logger

import play.api.libs.json.{Json, JsError}
import javax.ws.rs.{Path, Produces, PathParam}
import scala.concurrent.Future


// todo - test for these
@Path("user") // this will be the path to the doco
@Api(value = "/apitest/dogs", description = "look after the dogs",
  basePath = "xx",
  position = 2,
  produces = "application/json, application/xml",
  consumes = "application/json, application/xml",
  protocols = "http, https",
  authorizations = Array(new Authorization(value="oauth2",
    scopes = Array(
      new AuthorizationScope(scope = "vet", description = "vet access"),
      new AuthorizationScope(scope = "owner", description = "owner access")
    ))
  )
)
object DogController extends Controller {
  @ApiOperation(value = "addDog1",
    httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "dog", value = "Dog object to add", required = true, dataType = "Dog", paramType = "body")))
  def add1 = Action {
    request => Ok("test case")
  }

  @ApiOperation(value = "addDog2",
    notes = "Adds a dogs better",
    httpMethod = "PUT",
    nickname = "addDog2_nickname",
    authorizations = Array(new Authorization(value="oauth2",
      scopes = Array(
        new AuthorizationScope(scope = "vet", description = "vet access"),
        new AuthorizationScope(scope = "owner", description = "owner access")
      ))
    ),
    consumes = " application/json ",
    protocols = "http",
    position = 2)
  @ApiResponses(Array())
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "dog", value = "Dog object to add", required = true, dataType = "Dog", paramType = "body")))
  def add2 = Action {
    request => Ok("test case")
  }

  @ApiOperation(value = "Add a new Dog",
    notes = "Adds a dogs nicely",
    httpMethod = "PUT",
    authorizations = Array(new Authorization(value="oauth2",
      scopes = Array(
        new AuthorizationScope(scope = "vet", description = "vet access"),
        new AuthorizationScope(scope = "owner", description = "owner access")
      )),
      new Authorization(value="api_key")
    ),
    consumes = " application/json, text/yaml ",
    protocols = "http, https"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input"),
    new ApiResponse(code = 666, message = "Big Problem")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "dog", value = "Dog object to add", required = true, dataType = "Dog", paramType = "body")))
  def add3 = Action {
    request => Ok("test case")
  }

  @ApiOperation(value = "Updates a new Dog",
    notes = "Updates dogs nicely",
    httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "dog", value = "Dog object to update", required = true, dataType = "Dog", paramType = "body")))
  def update = Action {
    request => Ok("test case")
  }

  @ApiOperation(value = "Get Dog by Id",
    notes = "Returns a dog",
    response = classOf[Dog],
    httpMethod = "GET",
    produces = "")
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input"),
    new ApiResponse(code = 404, message = "Dog not found")))
  def get1(@ApiParam(value = "ID of dog to fetch", required = true) @PathParam("dogId") id: Long) = Action {
    request => Ok("test case")
  }

  @ApiOperation(value = "Get Dog by Id",
    notes = "Returns a dog",
    response = classOf[Dog],
    httpMethod = "GET",
    produces = "application/json")
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input"),
    new ApiResponse(code = 404, message = "Dog not found")))
  def get2(@ApiParam(value = "ID of dog to fetch", required = true) @PathParam("dogId") id: Long) = Action {
    request => Ok("test case")
  }

  @ApiOperation(value = "Get Dog by Id",
    notes = "Returns a dog",
    response = classOf[Dog],
    httpMethod = "GET",
    produces = "application/json, application/xml")
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input"),
    new ApiResponse(code = 404, message = "Dog not found")))
  def get3(@ApiParam(value = "ID of dog to fetch", required = true) @PathParam("dogId") id: Long) = Action {
    request => Ok("test case")
  }

  /*   Not Supported....routing is done elsewhere...
  - although could use this with a custom routing module
  -- one day..
  @GET
  @Path("/{petId}")
  */


  @ApiOperation(value = "List Dogs",
    nickname = "listDogs",
    notes = "Returns all dogs",
    response = classOf[Dog],
    responseContainer = "List",
    httpMethod = "GET")
  @Deprecated
  def list = Action {
    request => Ok("test case")
  }


  @ApiOperation(value = "Method with numeric chars in name",
    notes = "get a Dog with id 33",
    httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Dog not found")))
  def get33 = Action {
    request => Ok("test case")
  }

  // use the Jax.ws annotations
  // Delete a Dog
  @ApiOperation(value = "Delete", notes = "Deletes a user", httpMethod = "DELETE")
  def delete(
              @ApiParam(name = "dogId", value = "dogId") @PathParam("dogId") userId: String)
  = Action.async {
    implicit request => Future.successful(Ok)
  }

  @ApiOperation(value = "valueStr", notes = "notesStr", httpMethod = "GET")
  @Deprecated
  def deprecated = Action {
    request => Ok("test case")
  }

  def no_annotations = Action {
    request => Ok("test case")
  }

  def no_route = Action {
    request => Ok("test case")
  }

  @ApiOperation(value = "unknown method name",
    httpMethod = "UNKNOWN")
  def unknown_method() = Action {
    request => Ok("test case")
  }

  @ApiOperation(value = "undefined method name")
  def undefined_method() = Action {
    request => Ok("test case")
  }
}

case class Dog(id: Long, name: String)
