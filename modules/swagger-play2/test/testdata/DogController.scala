package test.testdata


import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException

import play.api.mvc.{Action, Controller}
import play.Logger

import play.api.libs.json.{Json, JsError}

/**
 * User: andyolliver
 * Date: 22/09/2013
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */

@Api(value = "/apitest/dogs", description = "requestToken")
object DogController extends Controller {

  @ApiOperation(value = "Add a new Dog",
    notes = "Adds a dogs nicely",
    httpMethod = "PUT")
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "dog", value = "Dog object to add", required = true, dataType = "Dog", paramType = "body")))
  def add = Action {
    request =>
      Ok("test case")
  }

  @ApiOperation(value = "Updates a new Dog",
      notes = "Updates dogs nicely",
      httpMethod = "POST")
    @ApiResponses(Array(
      new ApiResponse(code = 405, message = "Invalid input")))
    @ApiImplicitParams(Array(
      new ApiImplicitParam(name = "dog", value = "Dog object to update", required = true, dataType = "Dog", paramType = "body")))
    def update = Action {
      request =>
        Ok("test case")
    }

  @ApiOperation(value = "Get Dog by Id",
    notes = "Returns a dog",
    response = classOf[Dog],
    httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input"),
    new ApiResponse(code = 404, message = "Dog not found")))
  def get(id: Long) = Action {
    request =>
      Ok("test case")
  }

  @ApiOperation(value = "List Dogs",
    notes = "Returns all dogs",
    response = classOf[Dog],
    responseContainer = "List")
  def list = Action {
    request =>
      Ok("test case")
  }

}

case class Dog(id: Long, name: String)
