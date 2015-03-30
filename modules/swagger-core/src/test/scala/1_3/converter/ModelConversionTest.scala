package converter

import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._

import com.wordnik.swagger.util.Json

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import java.util.Date

import scala.annotation.meta.field

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class ModelConversionTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "format a date" in {
    val models = ModelConverters.getInstance().read(classOf[DateModel])
    val model = models.get("DateModel")
    model.getProperties().size should be (5)
    model should serializeToJson (
"""{
  "properties" : {
    "date" : {
      "type" : "string",
      "format" : "date-time",
      "position" : 1
    },
    "intValue" : {
      "type" : "integer",
      "format" : "int32",
      "position" : 2
    },
    "longValue" : {
      "type" : "integer",
      "format" : "int64",
      "position" : 3
    },
    "floatValue" : {
      "type" : "number",
      "format" : "float",
      "position" : 4
    },
    "doubleValue" : {
      "type" : "number",
      "format" : "double",
      "position" : 5
    }
  }
}""")
  }

  it should "format a set" in {
    val models = ModelConverters.getInstance().read(classOf[SetModel])
    val model = models.get("SetModel")
    model.getProperties().size should be (1)
    model should serializeToJson (
"""{
  "properties" : {
    "longs" : {
      "type" : "array",
      "uniqueItems" : true,
      "items" : {
        "type" : "integer",
        "format" : "int64"
      }
    }
  }
}""")
  }
}

case class DateModel(
  @(ApiModelProperty @field)(position=1) date: Date,
  @(ApiModelProperty @field)(position=2) intValue: Int,
  @(ApiModelProperty @field)(position=3) longValue: Long,
  @(ApiModelProperty @field)(position=4) floatValue: Float,
  @(ApiModelProperty @field)(position=5) doubleValue: Double)

case class SetModel(longs: Set[java.lang.Long])
