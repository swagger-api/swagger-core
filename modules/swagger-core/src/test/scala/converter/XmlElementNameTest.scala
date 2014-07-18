package converter

import converter.models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class XmlElementNameTest extends FlatSpec with ShouldMatchers {
  it should "honor XML Element Name" in {
    val models = ModelConverters.readAll(classOf[EB])
    models.size should be (1)
    val eb = models.filter(m => m.name == "EB").head
    val foo = eb.properties("foo")
    foo should not be (null)
  }
}
