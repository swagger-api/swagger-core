package converter

import converter.models._

import com.wordnik.swagger.model._
import com.wordnik.swagger.converter._

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.annotations.ApiModelProperty


import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class SubTypeModelTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "read a model with subTypes" in {
    val model = ModelConverters.read(classOf[Animal]).getOrElse(fail("no model found"))
    model.subTypes.size should be (2)
    println(write(model))
    write(model) should be ("""{"id":"Animal","description":"a model with subtypes","discriminator":"name","properties":{"name":{"type":"string","description":"name of animal"},"date":{"type":"string","format":"date-time","description":"date added"}},"subTypes":["DomesticAnimal","WildAnimal"]}""")

    /*
{
  "id": "Animal",
  "description": "a model with subtypes",
  "discriminator": "name",
  "properties": {
    "date": {
      "type": "string",
      "format": "date-time"
    },
    "name": {
      "type": "string"
    }
  },
  "subTypes": [
    "DomesticAnimal",
    "WildAnimal"
  ]
}
    */
  }
}