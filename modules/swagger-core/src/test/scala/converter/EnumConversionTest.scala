package converter

import converter.models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class EnumConversionTest extends FlatSpec with Matchers {
  it should "read a model with an enum property" in {
    val model = ModelConverters.read(classOf[ModelWithEnumProperty]).getOrElse(fail("no model found"))
    model.id should be ("ModelWithEnumProperty")
    model.properties.size should be (1)

    val enumValue = model.properties("enumValue")
    enumValue.`type` should be ("string")
    enumValue.required should be (false)
    enumValue.allowableValues should be (AllowableListValues(List("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY")))
  }
}