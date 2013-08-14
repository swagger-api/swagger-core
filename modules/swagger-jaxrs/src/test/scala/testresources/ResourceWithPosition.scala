package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@Path("/jaxb")
@Api(value = "/jaxb", description = "Jaxb resource", position = 1)
class ResourceWithPosition {
  @GET
  @ApiOperation(value = "gets something",
    notes = "No details provided",
    response = classOf[SomeModel])
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID", response = classOf[NotFoundModel]))
  )
  def getTest() = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }
}

case class SomeModel(id: Long, name: String)
