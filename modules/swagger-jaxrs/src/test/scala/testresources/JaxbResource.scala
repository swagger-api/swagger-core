package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.beans.BeanProperty

@Path("/jaxb")
@Api(value = "/jaxb", description = "Jaxb resource")
class JaxbResource {
  @GET
  @ApiOperation(value = "gets something",
    notes = "No details provided",
    response = classOf[CamelCaseModel])
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

@XmlRootElement(name = "camelCaseModel")
case class CamelCaseModel(id: Long, name: String)