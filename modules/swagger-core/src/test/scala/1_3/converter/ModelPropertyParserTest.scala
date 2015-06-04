package converter

import io.swagger.annotations.ApiModelProperty
import io.swagger.converter.ModelConverters
import io.swagger.models.properties.StringProperty
import io.swagger.util.Json
import models._

import io.swagger.converter._
import io.swagger.models._
import io.swagger.models.properties._

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.util.Date

import scala.collection.JavaConverters._
import scala.beans.BeanProperty

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class ModelPropertyParserTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "extract a string list" in {
    val property = ModelConverters.getInstance().readAsProperty(classOf[List[String]])
    property should serializeToJson (
"""{
  "type" : "array",
  "items" : {
    "type" : "object"
  }
}""")
  }

  it should "extract enum values from fields" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithEnumField])
    val model = models.get("ModelWithEnumField")
    val enumProperty = model.getProperties().get("enumValue")
    enumProperty.isInstanceOf[StringProperty] should be (true)

    val stringProperty = enumProperty.asInstanceOf[StringProperty]
    stringProperty.getEnum().asScala.toSet should equal (Set("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"))
  }

  it should "extract enum values from method return types" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithEnumProperty])
    val model = models.get("ModelWithEnumProperty")
    val enumProperty = model.getProperties().get("enumValue")
    enumProperty.isInstanceOf[StringProperty] should be (true)

    val stringProperty = enumProperty.asInstanceOf[StringProperty]
    stringProperty.getEnum().asScala.toSet should equal (Set("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"))
  }
}