package converter

import com.wordnik.swagger.converter._

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ListContainerConverterTest extends FlatSpec with Matchers {
  it should "read a case class with List" in {
    val model = ModelConverters.read(classOf[CaseClassWithList])
    model.get.properties.map{_._1}.toSet should equal (Set("id", "kidsAges"))
  }
}

case class CaseClassWithList(
  id: Long,
  kidsAges: List[java.lang.Integer]
)
