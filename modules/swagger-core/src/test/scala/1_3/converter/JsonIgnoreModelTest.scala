package converter

import io.swagger.converter.ModelConverters
import models._

import io.swagger.converter._

import io.swagger.util._

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class JsonIgnoreModelTest extends FlatSpec with Matchers {
  val models = ModelConverters.getInstance().read(classOf[ModelWithIgnoreAnnotation])
  models should serializeToJson (
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

