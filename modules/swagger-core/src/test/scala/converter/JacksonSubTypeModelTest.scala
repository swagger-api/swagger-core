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
class JacksonSubTypeModelTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "read a model with subTypes" in {
    val model = ModelConverters.read(classOf[JAnimal]).getOrElse(fail("no model found"))
    model.subTypes.size should be (2)

    write(model) should be ("""{"id":"JAnimal","discriminator":"type","properties":{"type":{"type":"string","description":"type of animal"},"date":{"type":"string","format":"date-time","description":"Date added to the zoo"}},"subTypes":["JWildAnimal","JDomesticAnimal"]}""")

/*
{
  "id": "JAnimal",
  "discriminator": "type",
  "properties": {
    "type": {
      "type": "string",
      "description": "type of animal"
    },
    "date": {
      "type": "string",
      "format": "date-time",
      "description": "Date added to the zoo"
    }
  },
  "subTypes": [
    "JWildAnimal",
    "JDomesticAnimal"
  ]
}
*/
  }
}