import models._
import models.composition.Pet;

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.converter._

import javax.xml.bind.annotation._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class XmlModelTests extends FlatSpec with Matchers {
  it should "process an XML model attribute" in {
    val schemas = ModelConverters.readAll(classOf[Monster])
    val model = schemas.get("Monster")

    model should not be (null)
    model.isInstanceOf[ModelImpl] should be (true)
    var xml = model.asInstanceOf[ModelImpl].getXml()

    xml should not be (null)
    xml.getName() should equal("monster")

    val property = model.getProperties().get("children")
    property should not be (null)
    xml = property.getXml()
    xml.getWrapped should equal (true)
    xml.getName() should be ("children")
  }
}

@XmlRootElement(name = "monster")
class Monster {
  @BeanProperty
  var name: String = _

  @XmlElementWrapper()
  @XmlElement(name = "children")
  @BeanProperty
  var children: java.util.List[String] = _

}

