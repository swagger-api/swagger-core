import models._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.util.Yaml
import com.wordnik.swagger.models.auth._

import com.fasterxml.jackson.databind.node.ObjectNode

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class AuthSerializationTest extends FlatSpec with Matchers {
  it should "convert serialize a basic auth model" in {
    val auth = new BasicAuthDefinition()
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"basic"}""")
  }

  it should "convert serialize a header key model" in {
    val auth = new ApiKeyAuthDefinition()
      .name("api-key")
      .in(In.HEADER)
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"apiKey","name":"api-key","in":"header"}""")
  }

  it should "convert serialize a header key model to yaml" in {
    val auth = new ApiKeyAuthDefinition().name("api-key").in(In.HEADER) 
    auth should serializeToYaml ("""---
type: "apiKey"
name: "api-key"
in: "header"
""")
  }

  it should "convert serialize an oauth2 implicit flow model" in {
    val auth = new OAuth2Definition().`implicit`("http://foo.com/authorization")
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"oauth2","authorizationUrl":"http://foo.com/authorization","flow":"implicit"}""")
  }

  it should "convert serialize an oauth2 password flow model" in {
    val auth = new OAuth2Definition().password("http://foo.com/token")
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"oauth2","tokenUrl":"http://foo.com/token","flow":"password"}""")
  }

  it should "convert serialize an oauth2 application flow model" in {
    val auth = new OAuth2Definition().application("http://foo.com/token")
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"oauth2","tokenUrl":"http://foo.com/token","flow":"application"}""")
  }

  it should "convert serialize an oauth2 accessCode flow model" in {
    val auth = new OAuth2Definition().accessCode("http://foo.com/authorizationUrl", "http://foo.com/token")
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"oauth2","authorizationUrl":"http://foo.com/authorizationUrl","tokenUrl":"http://foo.com/token","flow":"accessCode"}""")
  }

  it should "convert serialize an oauth2 implicit flow model with scopes" in {
    val auth = new OAuth2Definition()
      .`implicit`("http://foo.com/authorization")
      .scope("email", "read your email")
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"oauth2","authorizationUrl":"http://foo.com/authorization","flow":"implicit","scopes":{"email":"read your email"}}""")
  }

  it should "convert serialize an oauth2 password flow model with scopes" in {
    val auth = new OAuth2Definition()
      .password("http://foo.com/token")
      .scope("email", "read your email")
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"oauth2","tokenUrl":"http://foo.com/token","flow":"password","scopes":{"email":"read your email"}}""")
  }

  it should "convert serialize an oauth2 application flow model with scopes" in {
    val auth = new OAuth2Definition()
      .application("http://foo.com/token")
      .scope("email", "read your email")
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"oauth2","tokenUrl":"http://foo.com/token","flow":"application","scopes":{"email":"read your email"}}""")
  }

  it should "convert serialize an oauth2 accessCode flow model with scopes" in {
    val auth = new OAuth2Definition()
      .accessCode("http://foo.com/authorizationUrl", "http://foo.com/token")
      .scope("email", "read your email")
    Json.mapper.writeValueAsString(auth) should be ("""{"type":"oauth2","authorizationUrl":"http://foo.com/authorizationUrl","tokenUrl":"http://foo.com/token","flow":"accessCode","scopes":{"email":"read your email"}}""")
  }
}
