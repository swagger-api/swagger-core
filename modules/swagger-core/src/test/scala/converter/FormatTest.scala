package converter

import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._

import org.json4s.jackson.Serialization.write

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import java.util.Date

import scala.annotation.meta.field

@RunWith(classOf[JUnitRunner])
class FormatTest extends FlatSpec with Matchers {
  implicit val formats = SwaggerSerializers.formats

  it should "format a date" in {
    val model = ModelConverters.read(classOf[DateModel]).getOrElse(fail("no model found"))
    model.properties.size should be (5)

    write(model) should be ("""{"id":"DateModel","properties":{"date":{"type":"string","format":"date-time"},"intValue":{"type":"integer","format":"int32"},"longValue":{"type":"integer","format":"int64"},"floatValue":{"type":"number","format":"float"},"doubleValue":{"type":"number","format":"double"}}}""")
  }

  it should "format a set" in {
    val model = ModelConverters.read(classOf[SetModel]).getOrElse(fail("no model found"))
    model.properties.size should be (1)

    // gbolt; 11/9/2014 - Unclear why the type is expected to be "long"?  As far as I understand, long is not one of the
    // 7 primitive types defined in JSON Schema.  It should be integer with format of int64.
   // write(model) should be ("""{"id":"SetModel","properties":{"longs":{"type":"array","uniqueItems":true,"items":{"type":"long"}}}}""")
   write(model) should be ("""{"id":"SetModel","properties":{"longs":{"type":"array","uniqueItems":true,"items":{"type":"integer","format":"int64"}}}}""")

  }
}

case class DateModel(
  @(ApiModelProperty @field)(position=1) date: Date,
  @(ApiModelProperty @field)(position=2) intValue: Int,
  @(ApiModelProperty @field)(position=3) longValue: Long,
  @(ApiModelProperty @field)(position=4) floatValue: Float,
  @(ApiModelProperty @field)(position=5) doubleValue: Double)

case class SetModel(longs: Set[java.lang.Long])
