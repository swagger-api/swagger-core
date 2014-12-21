package testresources

import testmodels._
import com.wordnik.swagger.annotations._

import javax.ws.rs._

@Path("/enum_param")
@Api(value = "/enum_param", description = "Resource with Java Enum parameter")
class ResourceWithEnumParam {
  @GET
  @Path("/{access}/")
  @ApiOperation(value = "Get the enum query parameter reflected back",
    response = classOf[TestEnumPojo],
    notes = "No details provided",
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "object not found")))
  def getAccess(
    @ApiParam(value = "the access enum value") @QueryParam("access") @DefaultValue("PUBLIC") access: TestEnumPojo) = {
    access
  }
}
