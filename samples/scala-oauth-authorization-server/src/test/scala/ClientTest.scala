import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.apache.oltu.oauth2.client.request._
import org.apache.oltu.oauth2.common.message.types._

import com.sun.jersey.api.client.filter.LoggingFilter
import com.sun.jersey.api.client.{ Client, ClientResponse }

@RunWith(classOf[JUnitRunner])
class ClientTest extends FlatSpec with ShouldMatchers {
  val client = Client.create()
  client.addFilter(new LoggingFilter())

  it should "call the server" in {
    val request = OAuthClientRequest
      .tokenLocation("http://localhost:8002/oauth/token")
      .setGrantType(GrantType.AUTHORIZATION_CODE)
      .setClientId("someclientid")
      .setClientSecret("secret")
      .setCode("code")
      .setRedirectURI("http://localhost/redirect")
      // .setResponseType(ResponseType.CODE.toString())
      // .setState("state")
      .buildQueryMessage()
    val builder = client.resource(request.getLocationUri()).`type`("application/x-www-form-urlencoded")
    val response = builder.post(classOf[ClientResponse])
    println(response)
  }
}
