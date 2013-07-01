package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

@Path("/container")
@Api(value = "/container", description = "Container resource")
class ContainerResource {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = classOf[Sample],
    responseContainer = "List",
    position = 2)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[InvalidInputModel]),
    new ApiResponse(code = 404, message = "object not found", response = classOf[NotFoundModel])))
  def getTest(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")@PathParam("id") id: String) = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(List(out)).build
  }

  @POST
  @Path("/{id}")
  @ApiOperation(value = "Adds a new object",
    notes = "No return type",
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid object", response = classOf[InvalidInputModel])))
  def addTest(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")@PathParam("id") id: String,
    @ApiParam(value = "object to add", required = false)sample: Sample) = {
    Response.ok.build
  }
}
