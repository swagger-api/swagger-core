package testresources

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@Path("/basic")
@Api(value = "/basic", description = "Basic resource")
class BasicResource {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = classOf[Sample])
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiError(code = 404, reason = "object not found")))
  def getTest(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")@QueryParam("id") id: String) = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }

  @PUT
  @Path("/{id}")
  @ApiOperation(value = "Update by ID",
    notes = "No details provided")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID"),
    new ApiError(code = 404, reason = "object not found")))
  def updateTest(
    @ApiParam(value = "sample param data", required = true)sample: Sample) = {
  }
}
