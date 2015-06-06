package converter

import io.swagger.converter.ModelConverters
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RequiredFieldModelTest extends FlatSpec with Matchers {
  it should "apply read only flag when ApiProperty annotation first" in {
    val models = ModelConverters.getInstance().readAll(classOf[ApiFirstRequiredFieldModel])
    val model = models.get("aaa")
    val prop = model.getProperties().get("a")
    prop.getRequired() should be(true)
  }

  it should "apply read only flag when XmlElement annotation first" in {
    val models = ModelConverters.getInstance().readAll(classOf[XmlFirstRequiredFieldModel])
    val model = models.get("aaa")
    val prop = model.getProperties().get("a")
    prop.getRequired() should be(true)
  }
}
