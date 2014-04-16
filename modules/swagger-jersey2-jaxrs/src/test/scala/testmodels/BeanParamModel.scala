package testmodels

import com.wordnik.swagger.annotations.ApiParam
import javax.ws.rs.QueryParam
import java.util.Date

class BeanParamModel {
  @ApiParam(value = "sample set param")
  @QueryParam("ids")
  var ids: Set[String] = _

  @ApiParam(value = "sample date param")
  @QueryParam("startDate")
  var startDate: Date = _

  @ApiParam(value = "sample string param")
  @QueryParam("name")
  var name: String = _
}
