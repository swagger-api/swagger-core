package converter

import models._

import com.wordnik.swagger.converter._

import com.wordnik.swagger.util._

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class JsonPropertyModelTest extends FlatSpec with Matchers {
  val models = ModelConverters.getInstance().read(classOf[ModelWithJsonProperty])
  Json.pretty(models) should be (
"""{
  "ModelWithJsonProperty" : {
    "properties" : {
      "theCount" : {
        "type" : "integer",
        "format" : "int32"
      }
    }
  }
}""")
}
