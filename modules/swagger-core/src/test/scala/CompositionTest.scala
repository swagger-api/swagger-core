import models.composition._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.converter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class CompositionTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "read a model with required params and description" in {
    val schemas = ModelConverters.readAll(classOf[Human])
/*    Json.pretty(schemas) should equal (
"""{
  "Pet" : {
    "allOf" : [ {
      "$ref" : "Human"
    }, {
      "properties" : {
        "isDomestic" : {
          "type" : "boolean"
        }
      }
    } ]
  },
  "Human" : {
    "required" : [ "name" ],
    "properties" : {
      "name" : {
        "type" : "string",
        "description" : "The name of the human"
      },
      "type" : {
        "type" : "string"
      },
      "firstName" : {
        "type" : "string"
      },
      "lastName" : {
        "type" : "string"
      }
    },
    "discriminator" : "type"
  }
}"""
)
*/  }

  it should "read a model with composition" in {
    val schemas = ModelConverters.readAll(classOf[Animal])
    /*
    schemas.size should be (3)
    System.out.println("Pet")
    Json.prettyPrint(schemas.get("Pet"))
    System.out.println("Human")
    Json.prettyPrint(schemas.get("Human"))
    System.out.println("Animal")
    Json.prettyPrint(schemas.get("Animal"))*/
  }

}