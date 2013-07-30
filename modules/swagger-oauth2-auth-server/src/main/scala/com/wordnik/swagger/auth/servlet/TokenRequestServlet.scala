package com.wordnik.swagger.auth.servlet

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.JsonSerializer
import com.wordnik.swagger.auth.model.ApiResponseMessage
import com.wordnik.swagger.auth.service.AuthService

import org.apache.oltu.oauth2.as.issuer.{ MD5Generator, OAuthIssuerImpl }

import java.io.IOException

import javax.servlet.ServletException
import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }

@Api(value = "/oauth/requestToken", description = "requestToken")
class TokenRequestServlet extends HttpServlet {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  @ApiOperation(httpMethod = "GET", value = "Resource to request a token, given client_id and client_secret")
  @ApiResponses(Array(new ApiResponse(code = 405, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "client_id", value = "Client identifier", required = true, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "client_secret", value = "Client secret", required = true, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "redirect_uri", value = "Redirection URI", required = true, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "scope", value = "Scope for the token request", required = true, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "state", value = "Negotiation state", required = true, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "response_type", value = "Response Type", required = true, dataType = "string", paramType = "query")))
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    try{
      val apiResponse = new AuthService().authorize(request, response)
      response.setContentType("application/json")
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
      response.setDateHeader("Expires", 1);
      apiResponse.code match {
        case i: Int if(i < 400 && i >= 300) => response.sendRedirect(apiResponse.message)
        case _ => response.getOutputStream.write(JsonSerializer.asJson(apiResponse).getBytes("utf-8"))
      }
    }
    catch {
      case e: Exception => response.getOutputStream.write(JsonSerializer.asJson(ApiResponseMessage(400, e.getMessage)).getBytes("utf-8"))
    }
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override protected def doPost(request: HttpServletRequest, response: HttpServletResponse) = {
    doGet(request, response)
  }
}
