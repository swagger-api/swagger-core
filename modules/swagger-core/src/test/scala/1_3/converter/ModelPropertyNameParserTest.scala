package converter

import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.util.Json

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import java.util.Date

import scala.annotation.meta.field
import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class ModelPropertyNameParserTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "parse property names correctly per #415" in {
    val models = ModelConverters.getInstance().read(classOf[ModelPropertyNameClass])
    val model = models.get("ModelPropertyNameClass")
    val keys = model.getProperties().keySet.asScala.toSet
    keys should equal (Set("isometric", "is_persistent", "gettersAndHaters"))
  }
}

case class ModelPropertyNameClass(
  is_persistent: Boolean,
  isometric: Boolean,
  gettersAndHaters: String)
