import io.swagger.models.Swagger
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonDesrializationTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "deserialize the petstore" in {
    val s = m.readValue(new java.io.File("src/test/scala/specFiles/petstore.json"), classOf[Swagger])
    s.isInstanceOf[Swagger] should be(true)
  }

  it should "deserialize the composition test" in {
    val c = m.readValue(new java.io.File("src/test/scala/specFiles/compositionTest.json"), classOf[Swagger])
    c.isInstanceOf[Swagger] should be(true)

    // Json.prettyPrint(c)
  }
}
