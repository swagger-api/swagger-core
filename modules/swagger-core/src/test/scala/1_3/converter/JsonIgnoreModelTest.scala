package converter

import io.swagger.converter.ModelConverters
import matchers.SerializationMatchers._
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonIgnoreModelTest extends FlatSpec with Matchers {
  val models = ModelConverters.getInstance().read(classOf[ModelWithIgnoreAnnotation])
  models should serializeToJson(
    """{
  "ModelWithIgnoreAnnotation" : {
    "type":"object",
    "properties" : {
      "name" : {
        "type" : "string"
      }
    }
  }
}""")
}

