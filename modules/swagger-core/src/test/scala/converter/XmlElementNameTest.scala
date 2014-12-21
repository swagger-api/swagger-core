package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class XmlElementNameTest extends FlatSpec with Matchers {
  it should "honor XML Element Name" in {
    val models = ModelConverters.readAll(classOf[EB])
    models.size should be (1)
    val eb = models.filter(m => m.name == "EB").head
    val foo = eb.properties("foo")
    foo should not be (null)
  }

  // per https://github.com/swagger-api/swagger-core/issues/502
  it should "use the field name for both XmlElement and XmlAttribute annotations" in {
    val models = ModelConverters.readAll(classOf[XmlElementFieldModel])
    models.size should be (1)

    val eb = models.filter(m => m.name == "Pet").head
    val name = eb.properties("pet_name")
    name should not be (null)

    val age = eb.properties("pet_age")
    age should not be (null)
  }
}
