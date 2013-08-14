package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@Path("/basic")
@Api(value = "/basic", description = "Basic resource")
class ResourceWithArrayBody {
  @POST
  @Path("/{id}")
  @ApiOperation(value = "Update by ID",
    notes = "No details provided",
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID"),
    new ApiResponse(code = 404, message = "object not found")))
  def postTest(
    @ApiParam(value = "sample param data", required = true)sample: Array[java.lang.Integer]) = {
  }
}
