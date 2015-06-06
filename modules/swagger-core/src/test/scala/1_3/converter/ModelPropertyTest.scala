package converter

import java.util.Date

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.swagger.converter.ModelConverters
import io.swagger.models.properties._
import io.swagger.util.Json
import matchers.SerializationMatchers._
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ModelPropertyTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "extract properties" in {
    val models = ModelConverters.getInstance().readAll(classOf[Family])
    models.size should be(3)
    val person = models.get("Person")
    val employer = person.getProperties().get("employer")

    employer.isInstanceOf[ArrayProperty] should be(true)
    val employerProperty = employer.asInstanceOf[ArrayProperty]

    val items = employerProperty.getItems
    items.isInstanceOf[RefProperty] should be(true)
    val itemsProperty = items.asInstanceOf[RefProperty]
    itemsProperty.getSimpleRef() should equal("Employer")

    val awards = person.getProperties().get("awards")
    awards.isInstanceOf[ArrayProperty] should be(true)
    val awardsProperty = awards.asInstanceOf[ArrayProperty]
    awardsProperty.getItems().isInstanceOf[StringProperty] should be(true)
  }

  it should "extract a primitive array" in {
    val models = ModelConverters.getInstance().readAll(classOf[ModelWithPrimitiveArray])

    models.size should be(1)
    val model = models.get("ModelWithPrimitiveArray")
    val longArray = model.getProperties().get("longArray").asInstanceOf[ArrayProperty]
    val longArrayItems = longArray.getItems()
    longArrayItems.isInstanceOf[LongProperty] should be(true)

    val intArray = model.getProperties().get("intArray").asInstanceOf[ArrayProperty]
    val awardItems = intArray.getItems()
    awardItems.isInstanceOf[IntegerProperty] should be(true)
  }

  it should "read a model property" in {
    val models = ModelConverters.getInstance().readAll(classOf[IsModelTest])
    val model = models.get("IsModelTest")
    model should not be (null)
  }

  it should "read a scala object" in {
    val models = ModelConverters.getInstance().readAll(classOf[Pet])
    models.size should be(1)
    val model = models.get("Pet")
    model.getProperties().get("name") should not be (null)
  }
}

@RunWith(classOf[JUnitRunner])
class ModelPropertyOverrideTest extends FlatSpec with Matchers {
  it should "read a model with property dataTypes configured #679" in {
    val models = ModelConverters.getInstance().readAll(classOf[ModelWithModelPropertyOverrides])
    models should serializeToJson(
      """{
  "Children" : {
    "type": "object",
    "properties" : {
      "name" : {
        "type" : "string"
      }
    }
  },
  "ModelWithModelPropertyOverrides" : {
    "type": "object",
    "properties" : {
      "children" : {
        "type" : "array",
        "items" : {
          "$ref" : "#/definitions/Children"
        }
      }
    }
  }
}""")
  }
}

case class Family(membersSince: Date, members: List[Person])

class Pet {
  var name: String = _
  var age: Int = 0
  var birthday: java.util.Date = _
}

case class Person(
                   firstname: String,
                   lastname: String,
                   middlename: Option[String],
                   age: Int,
                   birthday: Date,
                   employer: List[Employer],
                   awards: List[String])

case class Employer(
                     name: String,
                     size: Int)

case class IsModelTest(
                        is_happy: Boolean,
                        name: String)