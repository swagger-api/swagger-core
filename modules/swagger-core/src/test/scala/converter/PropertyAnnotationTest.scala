package converter

import converter.models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import scala.beans.BeanProperty
import scala.collection.mutable.LinkedHashMap

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import javax.xml.bind.annotation._

@RunWith(classOf[JUnitRunner])
class PropertyAnnotationTest extends FlatSpec with ShouldMatchers {
  it should "read annotations on a property" in {
    val a = ModelConverters.readAll(classOf[ModelWithAnnotationOnProperty])
    JsonSerializer.asJson(a) should be ("""[{"id":"ModelWithAnnotationOnProperty","description":"my annotated model","properties":{"count":{"type":"integer","format":"int32","description":"the count of items"}}}]""")

/*
[
  {
    "id": "ModelWithAnnotationOnProperty",
    "description": "my annotated model",
    "properties": {
      "count": {
        "type": "integer",
        "format": "int32",
        "description": "the count of items"
      }
    }
  }
]
*/
  }
}
