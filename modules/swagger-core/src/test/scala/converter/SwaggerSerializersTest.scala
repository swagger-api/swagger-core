package converter

import com.wordnik.swagger.model._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.reflect.BeanProperty


@RunWith(classOf[JUnitRunner])
class SwaggerSerializersTest extends FlatSpec with ShouldMatchers {
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
}