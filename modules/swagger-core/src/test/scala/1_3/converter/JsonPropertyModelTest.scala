package converter

import io.swagger.converter.ModelConverters
import matchers.SerializationMatchers._
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonPropertyModelTest extends FlatSpec with Matchers {
  val models = ModelConverters.getInstance().read(classOf[ModelWithJsonProperty])
  models should serializeToJson(
    """{
  "ModelWithJsonProperty" : {
    "type":"object",
    "properties" : {
      "theCount" : {
        "type" : "integer",
        "format" : "int32"
      }
    }
  }
}""")
}
