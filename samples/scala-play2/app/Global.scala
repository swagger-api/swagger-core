import play.api._

import com.wordnik.swagger.config._
import com.wordnik.swagger.model._


object Global extends GlobalSettings {
  val info = ApiInfo(
    title = "Swagger Sample App",
    description = """This is a sample server Petstore server.  You can find out more about Swagger 
    at <a href="http://swagger.io">http://swagger.io</a> or on irc.freenode.net, #swagger.  For this sample,
    you can use the api key "special-key" to test the authorization filters""", 
    termsOfServiceUrl = "http://helloreverb.com/terms/",
    contact = "apiteam@wordnik.com", 
    license = "Apache 2.0", 
    licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.html")

  val oauth = OAuth(
    List(
      AuthorizationScope("write:pets", "Modify pets in your account"),
      AuthorizationScope("read:pets", "Read your pets")),
    List(
      ImplicitGrant(
        LoginEndpoint("http://petstore.swagger.wordnik.com/oauth/dialog"),
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
  ConfigFactory.config.info = Some(info)
}