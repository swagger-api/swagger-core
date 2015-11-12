package converter


import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import scala.beans.BeanProperty

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ByteConverterTest extends FlatSpec with Matchers {
  val models = ModelConverters.read(classOf[ByteConverterModel])
  println(JsonSerializer.asJson(models))
  JsonSerializer.asJson(models) should be ("""{"id":"ByteConverterModel","properties":{"myBytes":{"type":"array","items":{"type":"string"}}}}""")
}

class ByteConverterModel {
  @ApiModelProperty(dataType="List[string]")
  @BeanProperty var myBytes:Array[Byte] = _
}