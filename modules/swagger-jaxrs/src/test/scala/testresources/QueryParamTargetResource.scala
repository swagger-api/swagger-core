package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.{ Response, MediaType }

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@Path("/queryParamTest")
@Api(value = "/queryParamTest")
class QueryParamTargetResource {
  @ApiParam(value = "a class-level param", required = true, allowableValues = "a,b,c")
  @QueryParam("theParam") var classParam: String = _

  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = classOf[Sample],
    position = 0)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiResponse(code = 404, message = "object not found")))
  def getTest(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")@QueryParam("id") id: String) = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }
}
