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
class JsonIgnorePropertiesModelTest extends FlatSpec with Matchers {
  it should "ignore a property with ignore annotations" in {
    val models = ModelConverters.getInstance().read(classOf[ModelWithIgnorePropertiesAnnotation])
    models should serializeToJson (
"""{
  "ModelWithIgnorePropertiesAnnotation" : {
    "properties" : {
      "name" : {
        "type" : "string"
      },
      "favoriteAnimal" : {
        "type" : "string"
      }
    }
  }
}""")
  }

  it should "ignore a property with superclass #767" in {
    val models = ModelConverters.getInstance().read(classOf[Foo1])
    val model = models.get("Foo")
    model.getProperties().containsKey("bazField") should be (false)
  }
}