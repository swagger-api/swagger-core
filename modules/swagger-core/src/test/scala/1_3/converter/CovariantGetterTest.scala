package converter

import models._
import com.wordnik.swagger.util.Json

import com.wordnik.swagger.model._
import com.wordnik.swagger.converter._

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import models.JCovariantGetter

@RunWith(classOf[JUnitRunner])
class CovariantGetterTest extends FlatSpec with Matchers {
  val m = Json.mapper()
  m.registerModule(DefaultScalaModule)

  it should "read a getter with covariant return type" in {
    val models = ModelConverters.getInstance().read(classOf[JCovariantGetter.Sub])
    models.size should be (1)
Json.pretty(models) should be (
"""{
  "Sub" : {
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