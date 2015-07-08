import io.swagger.models.{PathImpl, RefPath, Swagger}
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

  it should "deserialize a path ref" in {
    val c = m.readValue(new java.io.File("src/test/scala/specFiles/pathRef.json"), classOf[Swagger])
    c.isInstanceOf[Swagger] should be(true)

    c.asInstanceOf[Swagger].getPath("/pet").isInstanceOf[RefPath] should be(true)
    c.asInstanceOf[Swagger].getPath("/user").isInstanceOf[PathImpl] should be(true)
  }
}
