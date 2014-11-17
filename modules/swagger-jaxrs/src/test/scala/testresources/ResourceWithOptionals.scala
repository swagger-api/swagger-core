package testresources

import javax.ws.rs.core.Response
import javax.ws.rs.{QueryParam, GET, Path}

import com.wordnik.swagger.annotations._

@Path("/optional")
@Api(value = "/optional", description = "Resource with optional query params")
class ResourceWithOptionals {
  @GET
  @Path("/test")
  @ApiOperation(value = "Test out optional query param",
    notes = "No details provided",
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "object not found")))
  def getMaybeString(@ApiParam(value = "optional string input", required = false) @QueryParam("maybeString") maybeString: Option[String]) = {
    Response.ok.entity(maybeString).build
  }
}