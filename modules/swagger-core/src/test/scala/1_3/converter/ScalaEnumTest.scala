package converter

import models._

import com.wordnik.swagger.models._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.util._

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ScalaEnumTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  ignore should "format a class with a Scala Enum type specifying allowable enum values" in {
    val models = ModelConverters.getInstance().readAll(classOf[SModelWithEnum])
    // model.properties.size should be(2)
    
    // Json.prettyPrint(models)
    // Desired behavior is to have the
    // "type": "string"
    // "enum": ["TALL", "GRANDE", "VENTI"]
    // modelStr should equal ("""{"id":"SModelWithEnum","description":"Scala model containing an Enumeration Value that is annotated with the dataType of the Enumeration class","properties":{"label":{"type":"string","description":"Textual label"},"orderSize":{"type":"string","description":"Order Size","enum":["GRANDE","TALL","VENTI"]}}}""")
/*
{
  "id": "SModelWithEnum",
  "description": "Scala model containing an Enumeration Value that is annotated with the dataType of the Enumeration class",
  "properties": {
    "label": {
      "type": "string",
      "description": "Textual label"
    },
    "orderSize": {
      "type": "string",
      "description": "Order Size",
      "enum": [
        "GRANDE",
        "TALL",
        "VENTI"
      ]
    }
  }
}*/
  }
/*
  it should "represent the Scala Enum Value type as a string without specifying allowable values if dataType is not specified" in {
    val models = ModelConverters.getInstance().readAll(classOf[SModelWithEnumNoDataType])
    // model.properties.size should be(2)
    // val modelStr = write(model)
    // modelStr should equal ("""{"id":"SModelWithEnumNoDataType","description":"Scala model containing an Enumeration Value that is not annotated with the dataType of the Enumeration class","properties":{"label":{"type":"string","description":"Textual label"},"orderSize":{"type":"string","description":"Order Size"}}}""")
  }

  it should "handle a Scala Enum Value type as a string without specifying allowable values if the dataType is badly specified" in {
    val models = ModelConverters.getInstance().readAll(classOf[SModelWithEnumBadDataType])
    // model.properties.size should be(2)
    // val modelStr = write(model)
    // If the dataType cannot be resolved to an Enumeration class, just gracefully behave as if the dataType is not specified
    // modelStr should equal ("""{"id":"SModelWithEnumBadDataType","description":"Scala model containing an Enumeration Value that is incorrectly annotated with a bad dataType of the Enumeration class","properties":{"label":{"type":"string","description":"Textual label"},"orderSize":{"type":"string","description":"Order Size"}}}""")
  }
*/
}