package converter

import java.util.UUID

import com.wordnik.swagger.annotations.ApiModelProperty
import com.wordnik.swagger.converter.ModelConverters
import com.wordnik.swagger.core.util.JsonSerializer
import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner

import scala.annotation.meta.field

@RunWith(classOf[JUnitRunner])
class UUIDConverterTest extends FlatSpec with Matchers {
  it should "read a generic model" in {
    val models = ModelConverters.readAll(classOf[ModelWithUUID])
    models.size should be(1)

    val model = models.head
    val nameProperty = model.properties("name")
    nameProperty.`type` should be("string")
    nameProperty.position should be(2)
    nameProperty.description should be(Some("name of the model"))

    val uuidProperty = model.properties("id")
    uuidProperty.`type` should be("string")
    uuidProperty.position should be(1)
    uuidProperty.required should be(right = true)
    uuidProperty.description should be(Some("uuid property"))

    println(JsonSerializer.asJson(models.head))
  }
}

case class ModelWithUUID(
  @(ApiModelProperty@field)(value = "name of the model", position = 2) name: String,
  @(ApiModelProperty@field)(value = "uuid property", required = true, position = 1) id: UUID)
