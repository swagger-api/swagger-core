package converter

import com.wordnik.swagger.core.SwaggerSpec
import com.wordnik.swagger.model._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.beans.BeanProperty


@RunWith(classOf[JUnitRunner])
class SwaggerSerializersTest extends FlatSpec with Matchers {
  implicit val formats = SwaggerSerializers.formats

  it should "serialize an authorization model" in {
    val auth = new Authorization("oauth2", Array(AuthorizationScope("scope1", "description 1"), 
      AuthorizationScope("scope2", "description 2")))
    println(pretty(render(parse(write(auth)))))/* should be (
"""{
  "type" : "oauth2",
  "scopes" : [ "scope1", "scope2" ],
  "description" : "the description"
}""")*/
  }

  it should "serialize an api listing with authorizations" in {
    val auth = new Authorization("oauth2", Array(AuthorizationScope("scope1", "description 1"),
      AuthorizationScope("scope2", "description 2")))
    val emptyList = List()
    val apiListing = ApiListing(
      "1.0",
      SwaggerSpec.version,
      "",
      "/relative-path-to-endpoint",
      emptyList,
      emptyList,
      emptyList,
      List(auth),
      emptyList,
      None,
      None,
      1)
    println(pretty(render(parse(write(apiListing)))))
  }

  it should "deserialize an ApiDeclaration" in {
    parse(apiDeclaration).extract[ApiListing] should not be (null)
  }

  val apiDeclaration = """{"apiVersion":"1.0.0","swaggerVersion":"1.2","basePath":"http://localhost:9095/resteasy","resourcePath":"/library","apis":[{"path":"/library/books/badger","operations":[{"method":"GET","summary":"gets books with Badger","notes":"gets books with @Badgerfish","type":"listing","nickname":"getBooksBadger","produces":["application/json"],"authorizations":{},"parameters":[],"responseMessages":[{"code":400,"message":"Not sure"},{"code":404,"message":"bad"}]}]},{"path":"/library/books/mapped","operations":[{"method":"GET","summary":"gets books with mapped","notes":"gets books with @Mapped","type":"listing","nickname":"getBooksMapped","produces":["application/json"],"authorizations":{},"parameters":[],"responseMessages":[{"code":400,"message":"Not sure"},{"code":404,"message":"bad"}]}]}],"models":{"book":{"id":"book","properties":{"author":{"type":"string"},"title":{"type":"string"},"iSBN":{"type":"string"}}},"listing":{"id":"listing","properties":{"books":{"type":"array","items":{"$ref":"book"}}}}}}"""
}