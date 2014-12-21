package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class RequiredFieldModelTest extends FlatSpec with Matchers {
  it should "apply read only flag when ApiProperty annotation first" in {
    val model = ModelConverters.read(classOf[ApiFirstRequiredFieldModel]).getOrElse(fail("no model found"))
    val prop = model.properties.getOrElse("a", fail("didn't find property a"))
    prop.required should be (true)
  }

  it should "apply read only flag when XmlElement annotation first" in {
    val model = ModelConverters.read(classOf[XmlFirstRequiredFieldModel]).getOrElse(fail("no model found"))
    val prop = model.properties.getOrElse("a", fail("didn't find property a"))
    prop.required should be (true)
  }
}
