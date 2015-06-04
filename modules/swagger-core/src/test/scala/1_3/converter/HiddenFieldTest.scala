package converter

import io.swagger.annotations.ApiModelProperty
import io.swagger.converter.ModelConverters
import io.swagger.models.properties.{LongProperty, StringProperty}
import models._

import io.swagger.converter._
import io.swagger.util._
import io.swagger.models._
import io.swagger.models.properties._
import io.swagger.annotations._

import java.util.Date

import scala.beans.BeanProperty

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class HiddenFieldTest extends FlatSpec with Matchers {
  it should "ignore a hidden field" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithHiddenFields])

    val model = models.get("ModelWithHiddenFields")
    model should not be (null)
    model.getProperties.size should be (2)

    val idValue = model.getProperties.get("id")
    idValue.isInstanceOf[LongProperty] should be (true)
    idValue.getRequired should be (true)

    val nameValue = model.getProperties.get("name")
    nameValue.isInstanceOf[StringProperty] should be (true)
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