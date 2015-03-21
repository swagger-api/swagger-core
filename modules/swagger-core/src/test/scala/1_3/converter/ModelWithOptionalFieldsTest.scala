package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.util.Json

import com.fasterxml.jackson.datatype.guava.GuavaModule

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class ModelWithOptionalFieldsTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(new GuavaModule())

  ignore should "read a model with guava optionals" in {
    val property = ModelConverters.getInstance().readAll(classOf[ModelWithOptionalFields])
    property should serializeToJson (
"""{
  "ModelWithOptionalFields" : {
    "id" : "ModelWithOptionalFields",
    "properties" : {
      "string" : {
        "type" : "string"
      },
      "integer" : {
        "type" : "integer",
        "format" : "int32"
      }
    }
  }
}""")
  }
}