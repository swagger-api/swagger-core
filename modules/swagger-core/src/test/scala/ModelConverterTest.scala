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
    m.writeValueAsString(schemas) should equal ("""{"Person":{"properties":{"id":{"type":"integer","format":"int64"},"firstName":{"type":"string"},"address":{"$ref":"Address"},"properties":{"type":"object","additionalProperties":{"type":"string"}},"birthDate":{"type":"string","format":"date-time"},"float":{"type":"number","format":"float"},"double":{"type":"number","format":"double"}}}}""")
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
    "required" : [ "name", "type", "isDomestic" ],
    "properties" : {
      "name" : {
        "type" : "string",
        "position" : 2,
        "description" : "The name of the pet"
      },
      "type" : {
        "type" : "string",
        "position" : 1,
        "description" : "The pet type"
      },
      "isDomestic" : {
        "type" : "boolean",
        "position" : 3
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
    "required" : [ "name", "type", "isDomestic" ],
    "properties" : {
      "clawCount" : {
        "type" : "integer",
        "format" : "int32"
      },
      "name" : {
        "type" : "string",
        "position" : 2,
        "description" : "The name of the pet"
      },
      "type" : {
        "type" : "string",
        "position" : 1,
        "description" : "The pet type"
      },
      "isDomestic" : {
        "type" : "boolean",
        "position" : 3
      }
    }
  }
}""")

  }
}