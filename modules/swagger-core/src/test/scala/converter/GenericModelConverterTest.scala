package converter

import model._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import scala.collection.mutable.LinkedHashMap

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class GenericModelConverterTest extends FlatSpec with ShouldMatchers {
  it should "read a generic model" in {
    val converter = new OverrideConverter

    val overrideModel = Model(
      id = "StringModel",
      name = "StringModel",
      qualifiedType = "model.StringModel",
      properties = LinkedHashMap("value" -> ModelProperty(
        `type` = "string",
        qualifiedType = "java.lang.String",
        position = 0,
        required = true)
      ),
      description = Some("A string model")
    )

    converter.add("model.StringModel", overrideModel)
    ModelConverters.addConverter(converter, true)

    val model = ModelConverters.read(classOf[StringModel]).getOrElse(fail("no model found"))
    model.qualifiedType should be ("model.StringModel")

    val value = model.properties("value")
    value.`type` should be ("string")
    value.required should be (true)
  }

  it should "override a model with json" in {
    val jsonString = """
{
  "id": "NumericModel",
  "name": "NumericModel",
  "properties": {
    "value": {
      "type": "integer",
      "required": false
    }
  }
}
"""
    val converter = new OverrideConverter
    converter.add("model.NumericModel", jsonString)

    ModelConverters.addConverter(converter, true)

    val model = ModelConverters.read(classOf[NumericModel]).getOrElse(fail("no model found"))
    model.qualifiedType should be ("model.NumericModel")

    val value = model.properties("value")
    value.`type` should be ("integer")
    value.required should be (false)  
  }
}
