package converter

import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util.JsonSerializer

import org.json4s.jackson.Serialization.write

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import java.util.Date

import scala.annotation.meta.field

@RunWith(classOf[JUnitRunner])
class ModelPropertyNameParserTest extends FlatSpec with Matchers {
  implicit val formats = SwaggerSerializers.formats

  it should "parse property names correctly per #415" in {
    val model = ModelConverters.read(classOf[ModelPropertyNameClass]).getOrElse(fail("no model found"))
    val keys = model.properties.keys.toSet
    (keys & Set("isometric", "is_persistent", "gettersAndHaters")).size should be (3)
  }
}

case class ModelPropertyNameClass(
  is_persistent: Boolean,
  isometric: Boolean,
  gettersAndHaters: String)
