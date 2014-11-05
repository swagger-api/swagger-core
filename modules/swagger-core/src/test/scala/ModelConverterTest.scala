import com.wordnik.swagger.converter._
import com.wordnik.swagger.util.Json
import models._
import models.composition.Pet
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import rapture.io.{Json => RJson}

@RunWith(classOf[JUnitRunner])
class ModelConverterTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model" in {
    val schemas = ModelConverters.read(classOf[Person])
    m.writeValueAsString(schemas) should equal( """{"Person":{"properties":{"id":{"type":"integer","format":"int64"},"firstName":{"type":"string"},"address":{"$ref":"Address"},"properties":{"type":"object","additionalProperties":{"type":"string"}},"birthDate":{"type":"string","format":"date-time"},"float":{"type":"number","format":"float"},"double":{"type":"number","format":"double"}}}}""")
  }

  it should "convert a model with Joda DateTime" in {
    val schemas = ModelConverters.read(classOf[JodaDateTimeModel])
    m.writeValueAsString(schemas) should equal( """{"JodaDateTimeModel":{"properties":{"createdAt":{"type":"string","format":"date-time"}}}}""")
  }

  it should "read an interface" in {
    val schemas = ModelConverters.readAll(classOf[Pet])
    val model: RJson = RJson.parse(Json.pretty(schemas))

    val pet: RJson = model.Pet
    pet.properties.name.`type`.get[String] should equal("string")
    pet.properties.`type`.`type`.get[String] should equal("string")
    pet.properties.isDomestic.`type`.get[String] should equal("boolean")
  }

  it should "read an inherited interface" in {
    val schemas = ModelConverters.readAll(classOf[Cat])

    val model: RJson = RJson.parse(Json.pretty(schemas))

    val pet: RJson = model.Cat
    pet.properties.clawCount.`type`.get[String] should equal("integer")
    pet.properties.clawCount.format.get[String] should equal("int32")
    pet.properties.name.`type`.get[String] should equal("string")
    pet.properties.`type`.`type`.get[String] should equal("string")
    pet.properties.isDomestic.`type`.get[String] should equal("boolean")
  }
}
