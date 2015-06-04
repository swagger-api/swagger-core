import io.swagger.models.Response
import io.swagger.util.Json
import models._

import io.swagger.util._
import io.swagger.models._
import io.swagger.models.properties._

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