package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.beans.BeanProperty

@Path("/basic")
@Api(value = "/basic", description = "Basic resource")
class ImplicitParamResource {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = classOf[Sample],
    position = 0)
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "id",
      value = "id of pet to add",
      required = true,
      dataType = "int",
      allowableValues = "a,b,c",
      paramType = "query")))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiResponse(code = 404, message = "object not found")))
  def getTest() = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }
}
