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
    val schemas = ModelConverters.getInstance().read(classOf[Person])
    Json.pretty(schemas).replace("\r", "") should equal(
"""{
  "Person" : {
    "properties" : {
      "id" : {
        "type" : "integer",
        "format" : "int64"
      },
      "firstName" : {
        "type" : "string"
      },
      "address" : {
        "$ref" : "#/definitions/Address"
      },
      "properties" : {
        "type" : "object",
        "additionalProperties" : {
          "type" : "string"
        }
      },
      "birthDate" : {
        "type" : "string",
        "format" : "date-time"
      },
      "float" : {
        "type" : "number",
        "format" : "float"
      },
      "double" : {
        "type" : "number",
        "format" : "double"
      }
    }
  }
}""".replace("\r", ""))
  }

  it should "convert a model with Joda DateTime" in {
    val schemas = ModelConverters.getInstance().read(classOf[JodaDateTimeModel])
    m.writeValueAsString(schemas) should equal ("""{"JodaDateTimeModel":{"properties":{"createdAt":{"type":"string","format":"date-time"}}}}""")
  }

  it should "read an interface" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Pet])
    Json.pretty(schemas).replace("\r", "") should equal (
"""{
  "Pet" : {
    "required" : [ "isDomestic", "name", "type" ],
    "properties" : {
      "type" : {
        "type" : "string",
        "position" : 1,
        "description" : "The pet type"
      },
      "name" : {
        "type" : "string",
        "position" : 2,
        "description" : "The name of the pet"
      },
      "isDomestic" : {
        "type" : "boolean",
        "position" : 3
      }
    }
  }
}""".replace("\r", ""))
  }

  it should "read an inherited interface" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Cat])
    Json.pretty(schemas).replace("\r", "") should equal (
"""{
  "Cat" : {
    "required" : [ "isDomestic", "name", "type" ],
    "properties" : {
      "clawCount" : {
        "type" : "integer",
        "format" : "int32"
      },
      "type" : {
        "type" : "string",
        "position" : 1,
        "description" : "The pet type"
      },
      "name" : {
        "type" : "string",
        "position" : 2,
        "description" : "The name of the pet"
      },
      "isDomestic" : {
        "type" : "boolean",
        "position" : 3
      }
    }
  }
}""".replace("\r", ""))

  }

  it should "honor the ApiModel name" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[ModelWithApiModel])
    schemas.size should be (1)
    val model = schemas.keySet().iterator().next()
    model should be ("MyModel")
  }

  it should "maintain property names" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[ModelPropertyName])
    schemas.size should be (1)

    val modelName = schemas.keySet().iterator().next()
    modelName should be ("ModelPropertyName")

    val model = schemas.get(modelName)

    val itr = model.getProperties().keySet().iterator()
    val prop1Name = itr.next()
    val prop2Name = itr.next()

    prop1Name should be ("is_persistent")
    prop2Name should be ("gettersAndHaters")
  }
}
