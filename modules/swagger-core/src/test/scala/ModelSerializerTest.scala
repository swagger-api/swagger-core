import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.converter._

import models._
import com.wordnik.swagger.util.Json

import scala.collection.mutable.HashMap
import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ModelSerializerTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model" in {
    val pet = new ModelImpl()
    val props = new HashMap[String, Property]
    props += "intValue" -> new IntegerProperty
    props += "longValue" -> new LongProperty
    props += "dateValue" -> new DateProperty
    props += "dateTimeValue" -> new DateTimeProperty
    pet.setProperties(props.asJava)
    pet.setRequired(List("intValue", "name").asJava)

    m.writeValueAsString(pet) should be ("""{"required":["intValue"],"properties":{"dateValue":{"type":"string","format":"date"},"longValue":{"type":"integer","format":"int64"},"dateTimeValue":{"type":"string","format":"date-time"},"intValue":{"type":"integer","format":"int32"}}}""")
  }

  it should "deserialize a model" in {
    val json = """{"required":["intValue"],"properties":{"dateValue":{"type":"string","format":"date"},"longValue":{"type":"integer","format":"int64"},"dateTimeValue":{"type":"string","format":"date-time"},"intValue":{"type":"integer","format":"int32"}}}"""

    val p = m.readValue(json, classOf[Model])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize an array model" in {
    val model = new ArrayModel()
    model.setItems(new RefProperty("Pet"))

    m.writeValueAsString(model) should be ("""{"type":"array","items":{"$ref":"#/definitions/Pet"}}""")
  }

  it should "deserialize an array model" in {
    val json = """{"type":"array","items":{"$ref":"#/definitions/Pet"}}"""
    val p = m.readValue(json, classOf[Model])
    p.isInstanceOf[ArrayModel] should be (true)
    m.writeValueAsString(p) should equal (json)
  }

  it should "not create an xml object for ref" in {
    val model = new RefModel("Monster")
    model.setDescription("oops")
    model.setExternalDocs(new ExternalDocs("external docs", "http://swagger.io"));

    Json.mapper().writeValueAsString(model) should be ("""{"$ref":"#/definitions/Monster"}""")
  }

  it should "make a field readOnly by annotation" in {
    val schemas = ModelConverters.getInstance().read(classOf[Car])
    Json.mapper().writeValueAsString(schemas) should be ("""{"Car":{"properties":{"wheelCount":{"type":"integer","format":"int32","readOnly":true}}}}""")
  }

  it should "serialize a model with a Set" in {
    val schemas = ModelConverters.getInstance().read(classOf[Manufacturers])
    Json.mapper().writeValueAsString(schemas) should be ("""{"Manufacturers":{"properties":{"countries":{"type":"array","uniqueItems":true,"items":{"type":"string"}}}}}""")
  }

  it should "deserialize a model with object example" in {
    val json = """{
      "title": "Error",
      "type": "object",
      "properties": {
        "code": {
          "type": "integer",
          "format": "int32"
        },
        "message": {
          "type": "string"
        },
        "fields": {
          "type": "string"
        }
      },
      "example": {
        "code": 1,
        "message": "hello",
        "fields": "abc"
      }
    }"""

    val model = Json.mapper().readValue(json, classOf[ModelImpl])
    Json.mapper().writeValueAsString(model.getExample()) should be ("""{"code":1,"message":"hello","fields":"abc"}""")
  }
}
