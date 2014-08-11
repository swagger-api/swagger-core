package converter

import model._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import org.joda.time.DateTime

import scala.collection.mutable.LinkedHashMap
import scala.annotation.meta.field


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class JodaDateTimeConverterTest extends FlatSpec with ShouldMatchers {
  it should "read a generic model" in {
    val models = ModelConverters.readAll(classOf[ModelWithJodaDateTime])
    models.size should be (1) // don't create a Joda DateTime object

    val model = models.head
    val nameProperty = model.properties("name")
    nameProperty.`type` should be ("string")
    nameProperty.position should be (2)
    nameProperty.description should be (Some("name of the model"))

    val dateTimeProperty = model.properties("createdAt")
    dateTimeProperty.`type` should be ("Date")
    dateTimeProperty.position should be (1)
    dateTimeProperty.required should be (true)
    dateTimeProperty.description should be (Some("creation timestamp"))

    println(JsonSerializer.asJson(models.head))
  }
}


case class ModelWithJodaDateTime (
  @(ApiModelProperty @field)(value = "name of the model", position = 2) name: String,
  @(ApiModelProperty @field)(value = "creation timestamp", required = true, position = 1) createdAt: DateTime)