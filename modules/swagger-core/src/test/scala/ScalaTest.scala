import models._
import models.composition.Pet;

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.converter._

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class ScalaTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model with scala list" in {
    val schemas = ModelConverters.getInstance().read(classOf[ClassWithScalaField])
    schemas should serializeToJson (
"""{
  "ClassWithScalaField" : {
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