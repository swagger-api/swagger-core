package com.wordnik.swagger.sample.servlet

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.sample.service.AuthService
import com.wordnik.swagger.sample.service.Cache
import com.wordnik.swagger.core.util.JsonSerializer

import org.apache.oltu.oauth2.as.issuer.{ MD5Generator, OAuthIssuerImpl }

import java.io.IOException

import javax.servlet.ServletException
import javax.servlet.http.{ Cookie, HttpServlet, HttpServletRequest, HttpServletResponse }


@Api(value = "/oauth/dialog", description = "requestToken")
class LoginDialogServlet extends HttpServlet {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  @ApiOperation(httpMethod = "GET", value = "renders a login dialog", produces = "text/html")
  @ApiResponses(Array(new ApiResponse(code = 400, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "scope", value = "Scope for access", defaultValue = "true", required = false, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "client_id", value = "Client identifier", required = true, dataType = "string", paramType = "query"),
    new ApiImplicitParam(name = "redirect_uri", value = "Redirection URI", required = true, dataType = "string", paramType = "query")))
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    val clientId = request.getParameter("client_id")
    val redirectUri = request.getParameter("redirect_uri")
    val scope = request.getParameter("scope")
    val output = new AuthService().dialog(clientId, redirectUri, scope)
    if(output.code == 200) {
      response.setContentType("text/html")
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
      response.setDateHeader("Expires", 1);
      response.getOutputStream.write(output.message.getBytes("utf-8"))
    }
    else if(output.code == 302) {
      val accessToken = output.message.split("access_token=")(1)
      val cookie = new Cookie("access_token", accessToken)
      cookie.setPath("/")
      response.addCookie(cookie)
      response.sendRedirect(output.message)
    }
  }
}