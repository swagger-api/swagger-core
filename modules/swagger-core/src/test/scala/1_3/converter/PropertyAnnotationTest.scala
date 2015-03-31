package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.util.Json
import com.wordnik.swagger.model._

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
