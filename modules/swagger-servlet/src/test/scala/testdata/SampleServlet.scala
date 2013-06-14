package testdata

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException

import javax.servlet.ServletException
import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }

@Api(value = "/oauth/requestToken", description = "requestToken")
class SampleServlet extends HttpServlet {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  @ApiOperation(
    value = "Add a new pet to the store",
    notes = "this adds pets nicely",
    httpMethod = "GET",
    response = classOf[Dog])
  @ApiResponses(Array(new ApiResponse(code = 405, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "pet",
      defaultValue = "fuzzy",
      value = "Pet object that needs to be added to the store",
      required = true,
      dataType = "Pet",
      paramType = "body")))
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override protected def doPost(request: HttpServletRequest, response: HttpServletResponse) = {
    doGet(request, response)
  }
}

case class Dog(id: Long, name: String)