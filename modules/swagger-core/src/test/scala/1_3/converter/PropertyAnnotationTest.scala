package converter

import io.swagger.converter.ModelConverters
import matchers.SerializationMatchers._
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PropertyAnnotationTest extends FlatSpec with Matchers {
  it should "read annotations on a property" in {
    val a = ModelConverters.getInstance().readAll(classOf[ModelWithAnnotationOnProperty])
    a should serializeToJson(
      """{
  "ModelWithAnnotationOnProperty" : {
    "type": "object",
    "properties" : {
      "count" : {
        "type" : "integer",
        "format" : "int32",
        "position" : 1,
        "description" : "the count of items"
      }
    },
    "description" : "my annotated model"
  }
}""")
  }
}
