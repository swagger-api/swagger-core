import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import matchers.SerializationMatchers._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ScalaTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model with scala list" in {
    val schemas = ModelConverters.getInstance().read(classOf[ClassWithScalaField])
    schemas should serializeToJson(
      """{
  "ClassWithScalaField" : {
    "type": "object",
    "properties" : {
      "id" : {
        "type" : "integer",
        "format" : "int32"
      },
      "name" : {
        "type" : "string"
      },
      "longField" : {
        "type" : "integer",
        "format" : "int64"
      },
      "listOfStrings" : {
        "type" : "array",
        "items" : {
          "type" : "string"
        }
      }
    }
  }
}"""
    )
  }
}

case class ClassWithScalaField(id: Int,
                               name: String,
                               longField: Long,
                               listOfStrings: List[String]
                                )