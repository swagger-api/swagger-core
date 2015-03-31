package converter

import models._

import com.wordnik.swagger.converter._

import com.wordnik.swagger.util._

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
    "properties" : {
      "name" : {
        "type" : "string"
      }
    }
  }
}""")
}

