package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.beans.BeanProperty

@Path("/merged")
@Api(value = "/merged", description = "MergedResource")
class MergedResource2 {
  @GET
  @Path("/model2")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = classOf[Sample2],
    position = 0)
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]),
    new ApiResponse(code = 404, message = "object not found")))
  def getTest(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")@DefaultValue("1") @QueryParam("id") id: String) = {
    val out = new Sample2
    out.id = "100"
    Response.ok.entity(out).build
  }
}

class Sample2 {
  @BeanProperty var id: String = _
}