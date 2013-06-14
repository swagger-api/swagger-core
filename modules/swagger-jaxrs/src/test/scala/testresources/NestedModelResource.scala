package testresources

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@Path("/family")
@Api(value = "/family", description = "Family Resource")
class NestedModelResource {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Gets a family by id",
    notes = "No details provided",
    response = classOf[Family])
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiResponse(code = 404, message = "object not found")))
  def getFamilyById(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,100]")@QueryParam("id") id: String) = {
    Response.ok.build
  }
}
