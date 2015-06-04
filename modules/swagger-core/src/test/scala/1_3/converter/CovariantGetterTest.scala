package converter

import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import models._

import io.swagger.model._
import io.swagger.converter._

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import models.JCovariantGetter

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class CovariantGetterTest extends FlatSpec with Matchers {
  val m = Json.mapper()
  m.registerModule(DefaultScalaModule)

  it should "read a getter with covariant return type" in {
    val models = ModelConverters.getInstance().read(classOf[JCovariantGetter.Sub])
    models.size should be (1)
    models should serializeToJson (
"""{
  "Sub" : {
    "type":"object",
    "properties" : {
      "myProperty" : {
        "type" : "integer",
        "format" : "int32",
        "position" : 1
      },
      "myOtherProperty" : {
        "type" : "integer",
        "format" : "int32",
        "position" : 2
      }
    }
  }
}""")
  }
}