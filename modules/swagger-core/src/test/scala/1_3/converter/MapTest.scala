package converter

import java.util.Date

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.swagger.converter.ModelConverters
import io.swagger.models.properties.{DateTimeProperty, LongProperty, MapProperty}
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MapTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "read a model with map and long value" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithLongMapValue])

    val model = models.get("ModelWithLongMapValue")
    val id = model.getProperties().get("id")
    id.isInstanceOf[LongProperty] should be(true)
    val p = model.getProperties().get("longMap")
    p.isInstanceOf[MapProperty] should be(true)
    val map = p.asInstanceOf[MapProperty]
    map.getAdditionalProperties().isInstanceOf[LongProperty] should be(true)
  }

  it should "read a model with map and date value" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithDateMapValue])

    val model = models.get("ModelWithDateMapValue")
    val id = model.getProperties().get("id")
    id.isInstanceOf[LongProperty] should be(true)
    val p = model.getProperties().get("dateMap")
    p.isInstanceOf[MapProperty] should be(true)
    val map = p.asInstanceOf[MapProperty]
    map.getAdditionalProperties().isInstanceOf[DateTimeProperty] should be(true)
  }
}

case class ModelWithLongMapValue(id: Long, longMap: Map[String, java.lang.Long])

case class ModelWithDateMapValue(id: Long, dateMap: Map[String, Date])
