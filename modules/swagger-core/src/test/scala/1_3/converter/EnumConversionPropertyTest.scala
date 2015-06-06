package converter

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import matchers.SerializationMatchers._
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EnumConversionPropertyTest extends FlatSpec with Matchers {
  val m = Json.mapper()
  m.registerModule(DefaultScalaModule)

  it should "read a model with an enum property" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithEnumProperty])
    models should serializeToJson(
      """{
  "ModelWithEnumProperty" : {
    "type": "object",
    "properties" : {
      "enumValue" : {
        "type" : "string",
        "enum" : [ "PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY" ]
      }
    }
  }
}""")
  }

  it should "read a model with enums" in {
    val models = ModelConverters.getInstance().read(classOf[ATM])
    models should serializeToJson(
      """{
  "ATM" : {
    "type": "object",
    "properties" : {
      "currency" : {
        "type" : "string",
        "enum" : [ "USA", "CANADA" ]
      }
    }
  }
}""")
  }
}
