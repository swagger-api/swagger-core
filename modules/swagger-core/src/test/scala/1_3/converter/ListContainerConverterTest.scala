package converter

import com.wordnik.swagger.converter._

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.wordnik.swagger.util.Json

import java.util.Date

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ListContainerConverterTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "read a case class with List" in {
    val models = ModelConverters.getInstance().read(classOf[CaseClassWithList])
    val keys = models.get("CaseClassWithList").getProperties().keySet().asScala.toSet
    keys should be (Set("id", "kidsAges"))
  }
}

case class CaseClassWithList(
  id: Long,
  kidsAges: List[java.lang.Integer]
)
