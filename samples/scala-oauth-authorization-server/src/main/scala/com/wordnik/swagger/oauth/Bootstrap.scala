package com.wordnik.swagger.oauth

import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.config._
import com.wordnik.swagger.model._

import com.wordnik.swagger.auth.service.ValidatorFactory

import javax.servlet.http.HttpServlet

class Bootstrap extends HttpServlet {
  val validator = new SampleValidator

  ValidatorFactory.validator = validator

  val oauth = OAuth(
    List(
      AuthorizationScope("write:pets", "Modify pets in your account"),
      AuthorizationScope("read:pets", "Read your pets")),
    List(
      ImplicitGrant(
        LoginEndpoint("http://localhost:8002/oauth/dialog"),
        "access_token"
      ),
      AuthorizationCodeGrant(
        TokenRequestEndpoint("http://petstore.swagger.wordnik.com/oauth/requestToken",
          "client_id",
          "client_secret"),
        TokenEndpoint("http://petstore.swagger.wordnik.com/oauth/token",
          "auth_code"
        )
    )
  ))
  ConfigFactory.config.authorizations = List(oauth)

  val info = ApiInfo(
    title = "Swagger Sample App",
    description = """This is a sample server Petstore server.  You can find out more about Swagger 
    at <a href="http://swagger.io">http://swagger.io</a> or on irc.freenode.net, #swagger.  For this sample,
    you can use the api key "special-key" to test the authorization filters""", 
    termsOfServiceUrl = "http://helloreverb.com/terms/",
    contact = "apiteam@wordnik.com", 
    license = "Apache 2.0", 
    licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.html")

  ConfigFactory.config.info = Some(info)
}
