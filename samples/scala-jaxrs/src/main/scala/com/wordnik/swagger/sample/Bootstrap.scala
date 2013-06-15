package com.wordnik.swagger.sample

import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.config._
import com.wordnik.swagger.model._

import javax.servlet.http.HttpServlet

class Bootstrap extends HttpServlet {
  val oauth = OAuth("http://localhost:8002/oauth/requestToken", "http://localhost:8002/oauth/token", List("PUBLIC"), List("IMPLICIT"))
  val apikey = ApiKey("api_key")
  ConfigFactory.config.authorizations = List(oauth, apikey)
}
