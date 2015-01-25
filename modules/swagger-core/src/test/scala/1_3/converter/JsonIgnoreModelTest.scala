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
class JsonIgnoreModelTest extends FlatSpec with Matchers {
  val models = ModelConverters.getInstance().read(classOf[ModelWithIgnoreAnnotation])
  Json.pretty(models) should equal (
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

