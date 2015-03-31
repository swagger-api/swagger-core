package converter

import models._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.util._
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._

import org.joda.time.DateTime

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.collection.mutable.LinkedHashMap
import scala.annotation.meta.field

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class JodaDateTimeConverterTest extends FlatSpec with Matchers {
  val m = Json.mapper()
  m.registerModule(DefaultScalaModule)

  it should "read a generic model" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithJodaDateTime])
    models.size should be (1) // don't create a Joda DateTime object

    val model = models.get("ModelWithJodaDateTime")

    val dateTimeProperty = model.getProperties.get("createdAt")
    dateTimeProperty.isInstanceOf[DateTimeProperty] should be (true)
    dateTimeProperty.getPosition should be (1)
    dateTimeProperty.getRequired should be (true)
    dateTimeProperty.getDescription should be ("creation timestamp")

    val nameProperty = model.getProperties.get("name")
    nameProperty.isInstanceOf[StringProperty] should be (true)
    nameProperty.getPosition should be (2)
    nameProperty.getDescription should be ("name of the model")
  }
}


case class ModelWithJodaDateTime (
  @(ApiModelProperty @field)(value = "name of the model", position = 2) name: String,
  @(ApiModelProperty @field)(value = "creation timestamp", required = true, position = 1) createdAt: DateTime)