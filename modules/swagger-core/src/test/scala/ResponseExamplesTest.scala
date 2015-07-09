import io.swagger.models.{ResponseImpl, Response}
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ResponseExamplesTest extends FlatSpec with Matchers {
  it should "create a response" in {
    val response = new ResponseImpl()
      .example("application/json", """{"name":"Fred","id":123456"}""")
    Json.mapper().writeValueAsString(response) should equal( """{"examples":{"application/json":"{\"name\":\"Fred\",\"id\":123456\"}"}}""")
  }
}