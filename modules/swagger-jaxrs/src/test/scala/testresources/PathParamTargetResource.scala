package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.{ Response, MediaType }

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@Path("/pathParamTest")
@Api(value = "/pathParamTest")
class PathParamTargetResource {
  @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")@PathParam("id") 
  var id: String = _

  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = classOf[Sample],
    position = 2)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiResponse(code = 404, message = "object not found")))
  def getTest(
    @ApiParam(value = "a query param", required = true, allowableValues = "a,b,c")@QueryParam("qp") classParam: String) = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }

  @GET
  @Path("/{id}/details")
  @ApiOperation(value = "Get details by ID",
    notes = "No details provided",
    response = classOf[Sample],
    position = 1)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiResponse(code = 404, message = "object not found")))
  def getDetails() = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }
}
