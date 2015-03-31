package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.annotations.ApiModelProperty

import com.fasterxml.jackson.module.scala.DefaultScalaModule


import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class MapTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "read a model with map and long value" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithLongMapValue])

    val model = models.get("ModelWithLongMapValue")
    val id = model.getProperties().get("id")
    id.isInstanceOf[LongProperty] should be (true)
    val p = model.getProperties().get("longMap")
    p.isInstanceOf[MapProperty] should be (true)
    val map = p.asInstanceOf[MapProperty]
    map.getAdditionalProperties().isInstanceOf[LongProperty] should be (true)
  }

  it should "read a model with map and date value" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithDateMapValue])

    val model = models.get("ModelWithDateMapValue")
    val id = model.getProperties().get("id")
    id.isInstanceOf[LongProperty] should be (true)
    val p = model.getProperties().get("dateMap")
    p.isInstanceOf[MapProperty] should be (true)
    val map = p.asInstanceOf[MapProperty]
    map.getAdditionalProperties().isInstanceOf[DateTimeProperty] should be (true)
  }
}

case class ModelWithLongMapValue(id: Long, longMap: Map[String, java.lang.Long])
case class ModelWithDateMapValue(id: Long, dateMap: Map[String, Date])
