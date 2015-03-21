package converter

import com.wordnik.swagger.converter._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.util._

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.beans.BeanProperty

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class ByteConverterTest extends FlatSpec with Matchers {
  val m = Json.mapper()
  m.registerModule(DefaultScalaModule)

  val models = ModelConverters.getInstance().read(classOf[ByteConverterModel])
  models should serializeToJson (
"""{
  "ByteConverterModel" : {
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
  @BeanProperty var myBytes:Array[java.lang.Byte] = _
}
