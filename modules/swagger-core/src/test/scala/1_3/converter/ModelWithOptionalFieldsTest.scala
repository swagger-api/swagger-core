package converter

import com.fasterxml.jackson.datatype.guava.GuavaModule
import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import matchers.SerializationMatchers._
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ModelWithOptionalFieldsTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(new GuavaModule())

  ignore should "read a model with guava optionals" in {
    val property = ModelConverters.getInstance().readAll(classOf[ModelWithOptionalFields])
    property should serializeToJson(
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