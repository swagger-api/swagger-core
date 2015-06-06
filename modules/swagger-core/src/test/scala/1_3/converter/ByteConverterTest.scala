package converter

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import matchers.SerializationMatchers._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class ByteConverterTest extends FlatSpec with Matchers {
  val m = Json.mapper()
  m.registerModule(DefaultScalaModule)

  val models = ModelConverters.getInstance().read(classOf[ByteConverterModel])
  models should serializeToJson(
    """{
  "ByteConverterModel" : {
    "type": "object",
    "properties" : {
      "myBytes" : {
        "type" : "array",
        "items" : {
          "type" : "string",
          "format" : "byte"
        }
      }
    }
  }
}""")
}

class ByteConverterModel {
  @BeanProperty var myBytes: Array[java.lang.Byte] = _
}
