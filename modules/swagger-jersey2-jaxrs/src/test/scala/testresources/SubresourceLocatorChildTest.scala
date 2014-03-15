package testresources

import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

@Api(value = "/child",
  description = "Media Type Test",
  produces = "application/json; charset=utf-8",
  protocols = "http, https")
class SubresourceLocatorChildTest {
  @GET
  @ApiOperation(value = "Return a string",
    notes = "No details provided",
    position = 0)
  def getTest =
    Response.ok.entity("ok").build
}