package com.wordnik.swagger.sample.resource


import com.wordnik.swagger.sample.service._
import com.wordnik.swagger.sample.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core._
import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.sample.exception.NotFoundException


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response
import javax.ws.rs.core.Context
import javax.ws.rs._

@Path("/data")
@Api(value = "/data", description = "Sample resource with protected data")
@Produces(Array("application/json;charset=utf8"))
class SampleResource {
  @GET
  @Path("/users/{userId}")
  @ApiOperation(value = "gets the logged-in user",
    response = classOf[User])
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def getUser(
    @ApiParam(value = "Authorization Code", required = true)@QueryParam("access_token") accessToken: String) = {

    new AuthService().validate(accessToken, {
      UserService.getUser().getOrElse(ApiResponseMessage(404, "user not found"))
    })
  }

  @GET
  @Path("/module/users")
  @Produces(Array("text/plain"))
  @ApiOperation(value = "gets a user",
    response = classOf[User])
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def getUserFragment(
    @ApiParam(value = "Authorization Code", required = true)@QueryParam("access_token") accessToken: String,
    @ApiParam(value = "ID of user to fetch", required = true) @PathParam("userId") userId: String,
    @Context request: HttpServletRequest) = {

    val token: String = {
      if(accessToken == "" || accessToken == null) {
          val h = request.getHeader("Authorization")
          if(h == null) ""
          else if(h.trim.toLowerCase.startsWith("bearer")) h.substring("bearer".length).trim
          else h.trim
        }
        else accessToken
      }

    try{
      new AuthService().validate(token, {
        UserService.getUser() match {
          case Some(e) => render(e)
          case _ => render(ApiResponseMessage(404, "user not found"))
        }
      })
    }
    catch {
      case e: Exception => render(ApiResponseMessage(401, "unauthorized"))
    }
  }

  def render(user: User): String = {
    val redirectUrl = "http://localhost:8002/oauth/dialog?redirect_uri=http://localhost:8000&client_id=someclientid&scope=email"
    val output = 
    <div>
       {
        if(user.firstName == "Anonymous") {
          <a href={redirectUrl}>Log in</a>
        }
        else {
          <a href="javascript:logout()">Log out</a>
        }
      }
      <div class="user">
        <ul id="foo">
          <li class="firstname">{user.firstName}</li>
          <li class="lastname">{user.lastName}</li>
          <li class="email">{user.email}</li>
        </ul>
      </div>
    </div>

    output.toString
  }

  def render(response: ApiResponseMessage): String = {
    val output = 
      <div class="error">
        <ul id="foo">
          <li class="code">{response.code}</li>
          <li class="message">{response.message}</li>
        </ul>
      </div>

    output.toString
  }
}