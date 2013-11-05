package testresources

import testmodels._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response


@Path("/withoutApiParams")
@Api(value = "/withoutApiParams", description = "Resource without ApiParam annotations")
class ResourceWithoutApiParams {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = classOf[Sample],
    position = 0)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiResponse(code = 404, message = "object not found")))
  def getTest(@DefaultValue("1") @QueryParam("id") id: String) = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }

  @PUT
  @Path("/{id}")
  @ApiOperation(value = "Update by ID",
    notes = "No details provided",
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID"),
    new ApiResponse(code = 404, message = "object not found")))
  def updateTest(@QueryParam("id") id: String, sample: Sample) = {
  }
}
