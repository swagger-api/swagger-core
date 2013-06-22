package com.wordnik.swagger.sample.servlet

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.sample.model.ApiResponseMessage
import com.wordnik.swagger.sample.service.AuthService

import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }

@Api(value = "/oauth/tokenStatus", description = "provides an oauth token status")
class TokenStatus extends HttpServlet {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  @ApiOperation(httpMethod = "GET", value = "returns an api status message", response = classOf[ApiResponseMessage])
  @ApiResponses(Array(new ApiResponse(code = 400, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "grant_type", value = "Grant type requested", allowableValues = "authorization_code,implicit", defaultValue = "authorization_code", required = false, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "client_id", value = "Client identifier", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "client_secret", value = "Client secret token", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "auth_code", value = "Authorization code provided by server", required = true, dataType = "string", paramType = "form")))
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    try {
    val accessCode = request.getCookies().filter(_.getName == "access_token") match {
      case e: Array[_] if(e.size > 0) => e.head.getValue
      case _ => request.getParameter("access_token")
    }
    val apiResponse = new AuthService().tokenStatus(accessCode)
    response.setContentType("application/json")
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
    response.setDateHeader("Expires", 1);
    response.getOutputStream.write(JsonSerializer.asJson(apiResponse).getBytes("utf-8"))}
    catch {
      case e: Exception => e.printStackTrace
    }
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override protected def doPost(request: HttpServletRequest, response: HttpServletResponse) = {
    doGet(request, response)
  }
}