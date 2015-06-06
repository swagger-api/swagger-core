package converter

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class ModelPropertyNameParserTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "parse property names correctly per #415" in {
    val models = ModelConverters.getInstance().read(classOf[ModelPropertyNameClass])
    val model = models.get("ModelPropertyNameClass")
    val keys = model.getProperties().keySet.asScala.toSet
    keys should equal(Set("isometric", "is_persistent", "gettersAndHaters"))
  }
}

case class ModelPropertyNameClass(
                                   is_persistent: Boolean,
                                   isometric: Boolean,
                                   gettersAndHaters: String)
