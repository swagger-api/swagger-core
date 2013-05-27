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
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiError(code = 404, reason = "object not found")))
  def getFamilyById(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,100]")@QueryParam("id") id: String) = {
    Response.ok.build
  }
}
