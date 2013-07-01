package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@Path("/standard")
@Api(value = "/standard", description = "Standard type resource")
class ResourceWithStandardTypes {
  @GET
  @Path("/{id}/strings")
  @ApiOperation(value = "Get array of strings",
    notes = "No details provided",
    response = classOf[String],
    responseContainer = "List",
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiResponse(code = 404, message = "object not found")))
  def getStringArray(
    @ApiParam(value = "sample int input", required = true, allowableValues = "range[0,10]")@QueryParam("id") id: List[java.lang.Integer]) = {
    val out = List("a", "b", "c")
    Response.ok.entity(out).build
  }

  @GET
  @Path("/{id}/doubles")
  @ApiOperation(value = "Gets array of doubles",
    notes = "No details provided",
    position = 2,
    response = classOf[Double],
    responseContainer = "List")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID"),
    new ApiResponse(code = 404, message = "object not found")))
  def getDoubleArray(
    @ApiParam(value = "sample boolean input", required = true)@QueryParam("bools")booleans: List[java.lang.Boolean]) = {
    val output = Array(1.0, 2.0, 3.0)
    Response.ok.entity(output).build
  }

  @POST
  @Path("/{id}/floats")
  @ApiOperation(value = "Posts list of floats",
    notes = "No details provided",
    position = 3)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID"),
    new ApiResponse(code = 404, message = "object not found")))
  def getDoubleArray(
    @ApiParam(value = "sample float list input", required = true)floats: java.util.List[Window]) = {
    val output = Array(1.0, 2.0, 3.0)
    Response.ok.entity(output).build
  }
}