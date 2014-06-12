package converter

import com.wordnik.swagger.annotations.ApiModelProperty
import com.wordnik.swagger.converter.ModelConverters
import com.wordnik.swagger.core.util.JsonSerializer
import org.joda.time.LocalDate
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

import scala.annotation.meta.field

@RunWith(classOf[JUnitRunner])
class JodaLocalDateConverterTest extends FlatSpec with ShouldMatchers {
  it should "read a generic model" in {
    val models = ModelConverters.readAll(classOf[ModelWithJodaLocalDate])
    models.size should be(1)

    val model = models.head
    val nameProperty = model.properties("name")
    nameProperty.`type` should be("string")
    nameProperty.position should be(2)
    nameProperty.description should be(Some("name of the model"))

    val dateTimeProperty = model.properties("createdAt")
    dateTimeProperty.`type` should be("Date")
    dateTimeProperty.position should be(1)
    dateTimeProperty.required should be(true)
    dateTimeProperty.description should be(Some("reation localDate"))

    println(JsonSerializer.asJson(models.head))
  }
}


case class ModelWithJodaLocalDate(
  @(ApiModelProperty@field)(value = "name of the model", position = 2) name: String,
  @(ApiModelProperty@field)(value = "creation localDate", required = true, position = 1) createdAt: LocalDate)
