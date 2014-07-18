package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.beans.BeanProperty

@Path("/standard")
@Api(value = "/standard", description = "Standard type resource")
class ResourceWithEnums {
  @GET
  @Path("/{id}/strings")
  @ApiOperation(value = "Get array of strings",
    notes = "No details provided",
    response = classOf[ModelWithEnumProperty],
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "object not found")))
  def getStringArray(
    @ApiParam(value = "sample int input", required = true, allowableValues = "range[0,10]")@QueryParam("id") id: List[java.lang.Integer]) = {
    val out = List("a", "b", "c")
    Response.ok.entity(out).build
  }
}