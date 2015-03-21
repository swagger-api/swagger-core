package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.util._

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class EnumConversionPropertyTest extends FlatSpec with Matchers {
  val m = Json.mapper()
  m.registerModule(DefaultScalaModule)

  it should "read a model with an enum property" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithEnumProperty])
    models should serializeToJson (
"""{
  "ModelWithEnumProperty" : {
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
    models should serializeToJson (
"""{
  "ATM" : {
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
