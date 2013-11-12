package converter

import converter.models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import com.wordnik.swagger.annotations.ApiModelProperty

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class MapTest extends FlatSpec with ShouldMatchers {
  it should "read a model with map and long value" in {
    val model = ModelConverters.read(classOf[ModelWithLongMapValue]).getOrElse(fail("no model found"))
    val id = model.properties("id")
    id.`type` should be ("long")
    val map = model.properties("longMap")
    map.`type` should be ("Map[string,long]")
  }

  it should "read a model with map and date value" in {
    val model = ModelConverters.read(classOf[ModelWithDateMapValue]).getOrElse(fail("no model found"))
    val id = model.properties("id")
    id.`type` should be ("long")
    val map = model.properties("dateMap")
    map.`type` should be ("Map[string,Date]")
  }
}

case class ModelWithLongMapValue(id: Long, longMap: Map[String, java.lang.Long])
case class ModelWithDateMapValue(id: Long, dateMap: Map[String, Date])
