package converter

import javax.xml.datatype.XMLGregorianCalendar

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.swagger.annotations.ApiModelProperty
import io.swagger.converter.ModelConverters
import io.swagger.models.properties.{DateTimeProperty, StringProperty}
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.annotation.meta.field

@RunWith(classOf[JUnitRunner])
class XMLGregorianCalendarTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "read a model with XMLGregorianCalendar" in {
    val models = ModelConverters.getInstance().readAll(classOf[ModelWithCalendar])
    models.size should be(1) // don't create a Joda DateTime object

    val model = models.get("ModelWithCalendar")
    val properties = model.getProperties()

    val nameProperty = properties.get("name")
    nameProperty.isInstanceOf[StringProperty] should be(true)
    nameProperty.getPosition() should be(2)
    nameProperty.getDescription() should be("name of the model")

    val dateTimeProperty = properties.get("createdAt")
    dateTimeProperty.isInstanceOf[DateTimeProperty] should be(true)
    dateTimeProperty.getPosition() should be(1)
    dateTimeProperty.getRequired() should be(true)
    dateTimeProperty.getDescription() should be("creation timestamp")
  }
}


case class ModelWithCalendar(
                              @(ApiModelProperty@field)(value = "name of the model", position = 2) name: String,
                              @(ApiModelProperty@field)(value = "creation timestamp", required = true, position = 1) createdAt: XMLGregorianCalendar)