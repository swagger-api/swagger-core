package com.wordnik.swagger.auth.servlet

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.auth.model.ApiResponseMessage
import com.wordnik.swagger.auth.service.AuthService

import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.{ Cookie, HttpServlet, HttpServletRequest, HttpServletResponse }

@Api(value = "/oauth/token", description = "provides an oauth token")
class TokenServlet extends HttpServlet {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  @ApiOperation(httpMethod = "POST", value = "Provides an oauth token", consumes = "application/x-www-form-urlencoded")
  @ApiResponses(Array(new ApiResponse(code = 400, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "grant_type", value = "Grant type requested", allowableValues = "authorization_code,implicit", defaultValue = "authorization_code", required = false, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "client_id", value = "Client identifier", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "client_secret", value = "Client secret token", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "auth_code", value = "Authorization code provided by server", required = true, dataType = "string", paramType = "form")))
  override protected def doPost(request: HttpServletRequest, response: HttpServletResponse) = {
    response.setContentType("application/json")
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
    response.setDateHeader("Expires", 1);
    try{
      val token = new AuthService().token(request, response)
      response.addCookie(new Cookie("access_token", token.access_token))
      response.getOutputStream.write(JsonSerializer.asJson(token).getBytes("utf-8"))
    }
    catch {
      case e: Exception => {
        response.setStatus(400)
        response.getOutputStream.write(JsonSerializer.asJson(ApiResponseMessage(400, e.getMessage)).getBytes("utf-8"))
      }
    }
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    response.setStatus(400)
    response.getOutputStream.write(JsonSerializer.asJson(ApiResponseMessage(400, "get requests are not supported")).getBytes("utf-8"))
  }
}