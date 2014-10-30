import models._
import models.composition.Pet;

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.converter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ModelConverterTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model" in {
    val schemas = ModelConverters.read(classOf[Person])
    m.writeValueAsString(schemas) should equal ("""{"Person":{"properties":{"id":{"type":"integer","format":"int64"},"firstName":{"type":"string"},"address":{"$ref":"Address"},"properties":{"type":"object","additionalProperties":{"type":"string"}},"birthDate":{"type":"string","format":"date-time"},"double":{"type":"number","format":"double"},"float":{"type":"number","format":"float"}}}}""")
  }

  it should "convert a model with Joda DateTime" in {
    val schemas = ModelConverters.read(classOf[JodaDateTimeModel])
    m.writeValueAsString(schemas) should equal ("""{"JodaDateTimeModel":{"properties":{"createdAt":{"type":"string","format":"date-time"}}}}""")
  }

  it should "read an interface" in {
    val schemas = ModelConverters.readAll(classOf[Pet])
    Json.pretty(schemas) should equal (
"""{
  "Pet" : {
    "properties" : {
      "isDomestic" : {
        "type" : "boolean"
      },
      "name" : {
        "type" : "string"
      },
      "type" : {
        "type" : "string"
      }
    }
  }
}""")
  }

  it should "read an inherited interface" in {
    val schemas = ModelConverters.readAll(classOf[Cat])
    Json.pretty(schemas) should equal (
"""{
  "Cat" : {
    "properties" : {
      "clawCount" : {
        "type" : "integer",
        "format" : "int32"
      },
      "isDomestic" : {
        "type" : "boolean"
      },
      "name" : {
        "type" : "string"
      },
      "type" : {
        "type" : "string"
      }
    }
  }
}""")

  }
}