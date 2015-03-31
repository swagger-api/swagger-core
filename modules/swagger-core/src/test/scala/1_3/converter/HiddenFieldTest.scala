package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.util._
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.annotations._

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