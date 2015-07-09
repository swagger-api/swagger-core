import io.swagger.models._
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonDeserializationTest extends FlatSpec with Matchers {
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
    c.asInstanceOf[Swagger].getPath("/pet").asInstanceOf[RefPath].get$ref() should equal("http://my.company.com/paths/health.json")
    c.asInstanceOf[Swagger].getPath("/user").isInstanceOf[PathImpl] should be(true)
  }

  it should "deserialize a response ref" in {
    val c = m.readValue(new java.io.File("src/test/scala/specFiles/responseRef.json"), classOf[Swagger])
    c.isInstanceOf[Swagger] should be(true)

    c.asInstanceOf[Swagger].getPath("/pet").getPut.getResponses.get("405").isInstanceOf[RefResponse] should be(true)
    c.asInstanceOf[Swagger].getPath("/pet").getPut.getResponses.get("405").asInstanceOf[RefResponse].get$ref() should equal("http://my.company.com/responses/errors.json#/method-not-allowed")
    c.asInstanceOf[Swagger].getPath("/pet").getPut.getResponses.get("404").isInstanceOf[RefResponse] should be(true)
    c.asInstanceOf[Swagger].getPath("/pet").getPut.getResponses.get("404").asInstanceOf[RefResponse].get$ref() should equal("http://my.company.com/responses/errors.json#/not-found")
    c.asInstanceOf[Swagger].getPath("/pet").getPut.getResponses.get("400").isInstanceOf[ResponseImpl] should be(true)
//    c.asInstanceOf[Swagger].getPath("/pet").asInstanceOf[RefPath].get$ref() should equal("http://my.company.com/paths/health.json")
  }

}
