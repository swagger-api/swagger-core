import com.wordnik.swagger.models._

import com.wordnik.swagger.util.Json

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class JsonDesrializationTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "deserialize the petstore" in {
    val s = m.readValue(new java.io.File("src/test/scala/specFiles/petstore.json"), classOf[Swagger])
    s.isInstanceOf[Swagger] should be (true)
  }
  
  it should "deserialize the composition test" in {
    val c = m.readValue(new java.io.File("src/test/scala/specFiles/compositionTest.json"), classOf[Swagger])
    c.isInstanceOf[Swagger] should be (true)
    
    // Json.prettyPrint(c)
  }
}
