package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.models._
import com.wordnik.swagger.util.Json

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class XmlElementNameTest extends FlatSpec with Matchers {
  it should "honor XML Element Name" in {
    val models = ModelConverters.getInstance().readAll(classOf[EB])
    models.size should be (1)

    val eb = models.get("EB")
    val bar = eb.getProperties().get("bar")
    val xml = bar.getXml()
    xml should not be (null)
    xml.getName() should equal ("foo")
  }

  // per https://github.com/swagger-api/swagger-core/issues/502
  it should "use the field name for both XmlElement and XmlAttribute annotations" in {
    val models = ModelConverters.getInstance().readAll(classOf[XmlElementFieldModel])
    models.size should be (1)

    val eb = models.get("XmlElementFieldModel")
    val name = eb.getProperties().get("name")
    val xmlName = name.getXml()
    xmlName should not be (null)
    xmlName.getName() should equal ("pet_name")

    val age = eb.getProperties().get("age")
    val xmlAge = age.getXml()
    xmlAge should not be (null)
    xmlAge.getName() should equal ("pet_age")

    eb.isInstanceOf[ModelImpl] should be (true)
    val impl = eb.asInstanceOf[ModelImpl]
    impl.getXml() should not be (null)
    impl.getXml().getName() should equal ("Pet")
  }
}
