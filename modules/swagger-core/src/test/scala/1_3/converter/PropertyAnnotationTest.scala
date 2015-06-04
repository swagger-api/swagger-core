package converter

import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import models._

import io.swagger.converter._
import io.swagger.model._

import io.swagger.annotations._
import io.swagger.converter._
import io.swagger.model._

import scala.beans.BeanProperty
import scala.collection.mutable.LinkedHashMap

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import javax.xml.bind.annotation._

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class PropertyAnnotationTest extends FlatSpec with Matchers {
  it should "read annotations on a property" in {
    val a = ModelConverters.getInstance().readAll(classOf[ModelWithAnnotationOnProperty])
    a should serializeToJson (
"""{
  "ModelWithAnnotationOnProperty" : {
    "type": "object",
    "properties" : {
      "count" : {
        "type" : "integer",
        "format" : "int32",
        "position" : 1,
        "description" : "the count of items"
      }
    },
    "description" : "my annotated model"
  }
}""")
  }
}
