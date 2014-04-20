package testresources

import javax.ws.rs._
import com.wordnik.swagger.annotations._
import testmodels._
import javax.ws.rs.core.Response

@Path("/beanParam")
@Api(value = "/beanParam", description = "Bean Param Resource")
class BeanParamResource {
  @GET
  @ApiOperation(value = "Search Object", notes = "No details provided", position = 0)
  def searchObject(@BeanParam params: BeanParamModel) {
    Response.ok.entity("ok").build
  }
}
