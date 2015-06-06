package converter

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import matchers.SerializationMatchers._
import models.JCovariantGetter
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CovariantGetterTest extends FlatSpec with Matchers {
  val m = Json.mapper()
  m.registerModule(DefaultScalaModule)

  it should "read a getter with covariant return type" in {
    val models = ModelConverters.getInstance().read(classOf[JCovariantGetter.Sub])
    models.size should be(1)
    models should serializeToJson(
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