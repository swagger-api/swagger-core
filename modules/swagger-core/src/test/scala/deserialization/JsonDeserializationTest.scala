import io.swagger.models.Swagger
import io.swagger.util.Json
import io.swagger.models.properties._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import scala.collection.JavaConverters._

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

  it should "deserialize a simple ObjectProperty" in {
    val json = """
    {
      "type": "object",
      "description" : "top level object",
      "properties" : {
        "property1" : {
          "type": "string",
          "description": "First property"
        },
        "property2" : {
          "type": "string",
          "description": "Second property"
        },
        "property3" : {
          "type": "string",
          "description": "Third property"
        }
      }
    }
    """
    val result = m.readValue(json, classOf[Property])
    result.isInstanceOf[ObjectProperty] should be(true)
    result.asInstanceOf[ObjectProperty].getProperties.asScala.size should be(3)

  }

  it should "deserialize nested ObjectProperty(s)" in {
    val json = """
    {
      "type": "object",
      "description" : "top level object",
      "properties" : {
        "property1" : {
          "type": "string",
          "description": "First property"
        },
        "property2" : {
          "type": "string",
          "description": "Second property"
        },
        "property3" : {
          "type": "object",
          "description": "Third property",
          "properties": {
            "property1" : {
              "type": "string",
              "description": "First nested property"
            }
          }
        }
      }
    }
    """
    val result = m.readValue(json, classOf[Property])
    result.isInstanceOf[ObjectProperty] should be(true)

    val topProperty = result.asInstanceOf[ObjectProperty]
    val firstLevelProperties = topProperty.getProperties.asScala
    firstLevelProperties.size should be(3)

    val property3 = firstLevelProperties("property3")
    property3.isInstanceOf[ObjectProperty] should be(true)

    val secondLevelProperties = property3.asInstanceOf[ObjectProperty].getProperties.asScala
    secondLevelProperties.size should be(1)
  }


}
