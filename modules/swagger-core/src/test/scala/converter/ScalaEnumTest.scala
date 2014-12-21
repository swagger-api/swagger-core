package converter

import models._

import com.wordnik.swagger.core.SwaggerSpec
import com.wordnik.swagger.core.util.ModelUtil
import com.wordnik.swagger.model._
import com.wordnik.swagger.converter._

import org.json4s._
import org.json4s.jackson.Serialization.write
import org.json4s.jackson._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ScalaEnumTest extends FlatSpec with Matchers {
  implicit val formats = SwaggerSerializers.formats

  "ModelConverters" should "format a class with a Scala Enum type specifying allowable enum values" in {
    val model = ModelConverters.read(classOf[SModelWithEnum]).getOrElse(fail("no model found"))
    model.properties.size should be(2)
    val modelStr = write(model)
    // Desired behavior is to have the
    // "type": "string"
    // "enum": ["TALL", "GRANDE", "VENTI"]
    modelStr should equal ("""{"id":"SModelWithEnum","description":"Scala model containing an Enumeration Value that is annotated with the dataType of the Enumeration class","properties":{"label":{"type":"string","description":"Textual label"},"orderSize":{"type":"string","description":"Order Size","enum":["GRANDE","TALL","VENTI"]}}}""")
  }

  it should "represent the Scala Enum Value type as a string without specifying allowable values if dataType is not specified" in {
    val model = ModelConverters.read(classOf[SModelWithEnumNoDataType]).getOrElse(fail("no model found"))
    model.properties.size should be(2)
    val modelStr = write(model)
    modelStr should equal ("""{"id":"SModelWithEnumNoDataType","description":"Scala model containing an Enumeration Value that is not annotated with the dataType of the Enumeration class","properties":{"label":{"type":"string","description":"Textual label"},"orderSize":{"type":"string","description":"Order Size"}}}""")
  }

  it should "handle a Scala Enum Value type as a string without specifying allowable values if the dataType is badly specified" in {
    val model = ModelConverters.read(classOf[SModelWithEnumBadDataType]).getOrElse(fail("no model found"))
    model.properties.size should be(2)
    val modelStr = write(model)
    // If the dataType cannot be resolved to an Enumeration class, just gracefully behave as if the dataType is not specified
    modelStr should equal ("""{"id":"SModelWithEnumBadDataType","description":"Scala model containing an Enumeration Value that is incorrectly annotated with a bad dataType of the Enumeration class","properties":{"label":{"type":"string","description":"Textual label"},"orderSize":{"type":"string","description":"Order Size"}}}""")
  }
}