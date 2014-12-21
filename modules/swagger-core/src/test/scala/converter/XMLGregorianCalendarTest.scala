package converter

import models._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import org.joda.time.DateTime

import scala.collection.mutable.LinkedHashMap
import scala.annotation.meta.field
import javax.xml.datatype.XMLGregorianCalendar

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class XMLGregorianCalendarTest extends FlatSpec with Matchers {
  it should "read a model with XMLGregorianCalendar" in {
    val models = ModelConverters.readAll(classOf[ModelWithCalendar])
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


case class ModelWithCalendar (
  @(ApiModelProperty @field)(value = "name of the model", position = 2) name: String,
  @(ApiModelProperty @field)(value = "creation timestamp", required = true, position = 1) createdAt: XMLGregorianCalendar)