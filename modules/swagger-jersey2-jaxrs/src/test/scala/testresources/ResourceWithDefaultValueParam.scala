package testresources

import testmodels._
import com.wordnik.swagger.annotations._

import javax.ws.rs._

@Path("/default_param")
@Api(value = "/default_param", description = "Resource with integer parameter with default value")
class ResourceWithDefaultValueParam {
  @GET
  @Path("/{id}/")
  @ApiOperation(value = "Get the integer query parameter reflected back",
    response = classOf[Integer],
    notes = "No details provided",
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "object not found")))
  def getAccess(
    @ApiParam(value = "the id value") @QueryParam("id") @DefaultValue("10") id: Integer) = {
    id
  }
}
