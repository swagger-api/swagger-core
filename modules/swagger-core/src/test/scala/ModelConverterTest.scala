import models._
import models.composition.Pet;

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._
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
    Json.prettyPrint(schemas)
    Json.pretty(schemas) should equal(
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
}""")
  }

  it should "convert a model with Joda DateTime" in {
    val schemas = ModelConverters.getInstance().read(classOf[JodaDateTimeModel])
    m.writeValueAsString(schemas) should equal ("""{"JodaDateTimeModel":{"properties":{"createdAt":{"type":"string","format":"date-time"}}}}""")
  }

  it should "read an interface" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Pet])
    Json.pretty(schemas) should equal (
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
}""")
  }

  it should "read an inherited interface" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Cat])
    Json.pretty(schemas) should equal (
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
}""")
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

    val itr = new java.util.TreeSet(model.getProperties().keySet()).iterator()
    val prop1Name = itr.next()
    val prop2Name = itr.next()

    prop1Name should be ("gettersAndHaters")
    prop2Name should be ("is_persistent")
  }
}

@RunWith(classOf[JUnitRunner])
class ModelConverterTest2 extends FlatSpec with Matchers {
  it should "seralize a parameterized type per 606" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Employee])
    val employee = schemas.get("employee").asInstanceOf[ModelImpl]
    val props = employee.getProperties()
    val et = props.keySet().iterator()
    
    val id = props.get(et.next())
    id.getClass should be (classOf[IntegerProperty])

    val firstName = props.get(et.next())
    firstName.getClass should be (classOf[StringProperty])

    val lastName = props.get(et.next())
    lastName.getClass should be (classOf[StringProperty])

    val department = props.get(et.next())
    department.getClass should be (classOf[RefProperty])

    val manager = props.get(et.next())
    manager.getClass should be (classOf[RefProperty])

    val team = props.get(et.next())
    team.getClass should be (classOf[ArrayProperty])
    val ap = team.asInstanceOf[ArrayProperty]
    ap.getUniqueItems should equal (true)

    employee.getXml should not be (null)
    employee.getXml.getName should be ("employee")
  }
}