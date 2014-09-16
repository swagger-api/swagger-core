import models._

import com.wordnik.swagger.util._
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ResponseExamplesTest extends FlatSpec with Matchers {
  it should "create a response" in {
    val response = new Response()
      .example("application/json", """{"name":"Fred","id":123456"}""")
    Json.mapper().writeValueAsString(response) should equal("""{"examples":{"application/json":"{\"name\":\"Fred\",\"id\":123456\"}"}}""")
  }
}