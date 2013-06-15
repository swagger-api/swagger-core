package com.wordnik.swagger.sample

import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.config._
import com.wordnik.swagger.model._

import javax.servlet.http.HttpServlet

class Bootstrap extends HttpServlet {
  val oauth = OAuth(
    List("PUBLIC"),
    List(
      ImplicitGrant(
        LoginEndpoint("http://localhost:8002/oauth/dialog"),
        "access_code"
      ),
      AuthorizationCodeGrant(
        TokenRequestEndpoint("http://localhost:8002/oauth/requestToken",
          "client_id",
          "client_secret"),
        TokenEndpoint("http://localhost:8002/oauth/token",
          "access_code"
        )
    )
  ))
  val apikey = ApiKey("api_key")
  ConfigFactory.config.authorizations = List(oauth, apikey)
}
