package converter

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class ListContainerConverterTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "read a case class with List" in {
    val models = ModelConverters.getInstance().read(classOf[CaseClassWithList])
    val keys = models.get("CaseClassWithList").getProperties().keySet().asScala.toSet
    keys should be(Set("id", "kidsAges"))
  }
}

case class CaseClassWithList(
                              id: Long,
                              kidsAges: List[java.lang.Integer]
                              )
