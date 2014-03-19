package converter

import converter.models._

import com.wordnik.swagger.model._
import com.wordnik.swagger.converter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import converter.models.JCovariantGetter

@RunWith(classOf[JUnitRunner])
class CovariantGetterTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "read a getter with covariant return type" in {
    val model = ModelConverters.read(classOf[JCovariantGetter.Sub]).getOrElse(fail("no model found"))
    val myProperty = model.properties.get("myProperty")
    myProperty should not be (None)
    myProperty.get.qualifiedType should be ("java.lang.Integer")
    val myOtherProperty = model.properties.get("myOtherProperty")
    myOtherProperty should not be (None)
    myOtherProperty.get.qualifiedType should be ("java.lang.Integer")
  }

}