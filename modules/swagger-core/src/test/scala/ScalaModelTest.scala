import models._
import models.composition.Pet;

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.converter._

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class ScalaModelTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "convert a simple scala case class" in {
    val schemas = ModelConverters.getInstance().read(classOf[SimpleCaseClass])
    schemas should serializeToJson (
"""{
  "SimpleCaseClass" : {
    "properties" : {
      "name" : {
        "type" : "string"
      },
      "count" : {
        "type" : "integer",
        "format" : "int32"
      }
    }
  }
}""")
  }

  it should "convert a scala case class with List property" in {
    val schemas = ModelConverters.getInstance().read(classOf[CaseClassWithList])
    schemas should serializeToJson (
"""{
  "CaseClassWithList" : {
    "properties" : {
      "name" : {
        "type" : "string"
      },
      "items" : {
        "type" : "array",
        "items" : {
          "type" : "string"
        }
      }
    }
  }
}""")
  }

  it should "convert a scala case class with optional value" in {
    val schemas = ModelConverters.getInstance().read(classOf[CaseClassWithOptionLong])
    val props = (schemas.get("CaseClassWithOptionLong")).asInstanceOf[ModelImpl].getProperties()
    val propertyCount = props.keySet.size

    val keys = props.keySet.asScala.toList
    keys(0) should be ("intValue")
    keys(1) should be ("longValue")
    keys(2) should be ("setValue")
    keys(3) should be ("dateValue")
    keys(4) should be ("booleanValue")

    schemas should serializeToJson (
"""{
  "CaseClassWithOptionLong" : {
    "properties" : {
      "intValue" : {
        "type" : "integer",
        "format" : "int32"
      },
      "longValue" : {
        "type" : "array",
        "items" : {
          "type" : "object"
        }
      },
      "setValue" : {
        "type" : "array",
        "uniqueItems" : true,
        "items" : {
          "type" : "string"
        }
      },
      "dateValue" : {
        "type" : "string",
        "format" : "date-time"
      },
      "booleanValue" : {
        "type" : "boolean"
      }
    }
  }
}""")
  }

  it should "convert a scala case class with nested models" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[NestedModel])
    schemas should serializeToJson ( 
"""{
  "ComplexModel" : {
    "properties" : {
      "name" : {
        "type" : "string"
      },
      "age" : {
        "type" : "integer",
        "format" : "int32"
      }
    }
  },
  "NestedModel" : {
    "properties" : {
      "complexModel" : {
        "$ref" : "#/definitions/ComplexModel"
      },
      "localtime" : {
        "type" : "string",
        "format" : "date-time"
      }
    }
  }
}""")
  }

  it should "read an interface" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Pet])
    schemas should serializeToJson (
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
}
