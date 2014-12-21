package converter

import converter.models._

import com.wordnik.swagger.model._
import com.wordnik.swagger.converter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import converter.models.JCovariantGetter

@RunWith(classOf[JUnitRunner])
class CovariantGetterTest extends FlatSpec with Matchers {
  implicit val formats = SwaggerSerializers.formats

  it should "read a getter with covariant return type" in {
    val model = ModelConverters.read(classOf[JCovariantGetter.Sub]).getOrElse(fail("no model found"))
    val myProperty = model.properties.getOrElse("myProperty", fail("didn't get myProperty"))
    myProperty.get.qualifiedType should be ("java.lang.Integer")

    val myOtherProperty = model.properties.getOrElse("myOtherProperty", fail("didn't find myOtherProperty"))
    myOtherProperty.get.qualifiedType should be ("java.lang.Integer")
  }
}