package converter

import com.wordnik.swagger.converter._
import com.wordnik.swagger.model.ModelRef
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class VectorContainerConverterTest extends FlatSpec with Matchers {
  it should "read a case class with Vector" in {
    val model = ModelConverters.read(classOf[CaseClassWithvector]).getOrElse(fail("no model found"))
    model.properties.map{_._1}.toSet should equal(Set("id", "kidsAges"))
    val properties = model.properties("kidsAges")

    properties.`type` should equal("Vector")
    properties.items should be(Some(ModelRef("int", None, Some("java.lang.Integer"))))
  }
}

case class CaseClassWithvector(
                                id: Long,
                                kidsAges: Vector[java.lang.Integer]
                                )
