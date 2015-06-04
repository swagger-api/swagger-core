import io.swagger.converter._
import io.swagger.converter.ModelConverters
import matchers.SerializationMatchers._
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ModelWithReferenceTest extends FlatSpec with Matchers {
  it should "convert a model with reference property" in {
    val schemas = ModelConverters.getInstance().read(classOf[ModelWithReference])
    schemas should serializeToJson (
      """{
          "ModelWithReference": {
            "type": "object",
            "properties": {
              "description": {
                "$ref": "http://swagger.io/schemas.json#/Models/Description"
              }
            }
          }
        }""")
  }

  it should "convert a model with reference and reference property" in {
    val schemas = ModelConverters.getInstance().read(classOf[ModelContainingModelWithReference])
    schemas should serializeToJson (
      """{
          "ModelContainingModelWithReference": {
            "type": "object",
            "properties": {
              "model": {
                "$ref": "http://swagger.io/schemas.json#/Models"
              },
              "anotherModel": {
                "$ref": "http://swagger.io/schemas.json#/Models/AnotherModel"
              }
            }
          }
        }""")
  }
}
