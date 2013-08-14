package converter

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import java.util.Date

import scala.annotation.target.field

import scala.collection.mutable.LinkedHashMap

@RunWith(classOf[JUnitRunner])
class FormatTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "format a date" in {
    val model = ModelConverters.read(classOf[DateModel]).getOrElse(fail("no model found"))
    model.properties.size should be (5)

    write(model) should be ("""{"id":"DateModel","properties":{"date":{"type":"string","format":"date-time"},"intValue":{"type":"integer","format":"int32"},"longValue":{"type":"integer","format":"int64"},"floatValue":{"type":"number","format":"float"},"doubleValue":{"type":"number","format":"double"}}}""")
  }
}

case class DateModel(
  @(ApiModelProperty @field)(position=1) date: Date,
  @(ApiModelProperty @field)(position=2) intValue: Int,
  @(ApiModelProperty @field)(position=3) longValue: Long,
  @(ApiModelProperty @field)(position=4) floatValue: Float,
  @(ApiModelProperty @field)(position=5) doubleValue: Double)
