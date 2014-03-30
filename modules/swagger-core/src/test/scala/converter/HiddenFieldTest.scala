package converter

import converter.models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._

import java.util.Date

import scala.reflect.BeanProperty

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class HiddenFieldTest extends FlatSpec with ShouldMatchers {
  it should "ignore a hidden field" in {
    val model = ModelConverters.read(classOf[ModelWithHiddenFields]).getOrElse(fail("no model found"))
    model.id should be ("ModelWithHiddenFields")
    model.properties.size should be (2)

    val idValue = model.properties("id")
    idValue.`type` should be ("long")
    idValue.required should be (true)

    val nameValue = model.properties("name")
    nameValue.`type` should be ("string")
    nameValue.required should be (true)
  }
}

class ModelWithHiddenFields {
  @BeanProperty
  @ApiModelProperty(required=true)
  var id: Long = _

  @BeanProperty
  @ApiModelProperty(required=true, hidden=false)
  var name: String = _

  @BeanProperty
  @ApiModelProperty(hidden=true)
  var password: String = _
}