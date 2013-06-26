package model

import com.wordnik.swagger.model._
import com.wordnik.swagger.converter.ModelInheritenceUtil

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.mutable.LinkedHashMap

@RunWith(classOf[JUnitRunner])
class ModelInheritenceTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "serialize a model with a super class" in {
    val props = Map(
      "name" -> ModelProperty(
                  `type` = "string",
                  qualifiedType = "string"),
      "id" -> ModelProperty(
                  `type` = "long",
                  qualifiedType = "long")
    )
    val properties = new LinkedHashMap[String, ModelProperty]
    properties ++= props

    val model = Model(
      id = "CatModel",
      name = "CatModel",
      qualifiedType = "com.super.CatModel",
      description = Some("A cat model"),
      baseModel = Some("AnimalBaseModel"),
      properties = properties
    )

    write(model) should be ("""{"id":"CatModel","name":"CatModel","properties":{"name":{"type":"string","required":false},"id":{"type":"long","required":false}},"description":"A cat model","extends":"AnimalBaseModel"}""")
  }

  it should "deserialize a model with a super class" in {
    val json = """{"id":"CatModel","name":"CatModel","qualifiedType":"com.super.CatModel","properties":{"name":{"type":"string","required":false},"id":{"type":"long","required":false}},"description":"A cat model","extends":"AnimalBaseModel"}"""

    parse(json).extract[Model] match {
      case e: Model => {
        e.id should be ("CatModel")
        e.name should be ("CatModel")
        e.qualifiedType should be ("com.super.CatModel")
        e.description should be (Some("A cat model"))
        e.baseModel should be (Some("AnimalBaseModel"))
        e.properties.size should be (2)
        val name = e.properties("name")
        name.`type` should be ("string")
        name.qualifiedType should be ("string")

        val id = e.properties("id")
        id.`type` should be ("long")
        id.qualifiedType should be ("long")
      }
    }
  }

  it should "serialize a base model" in {
     val props = Map(
      "id" -> ModelProperty(
        `type` = "long",
        qualifiedType = "long")
    )
    val properties = new LinkedHashMap[String, ModelProperty]
    properties ++= props

    val model = Model(
      id = "AnimalBaseModel",
      name = "AnimalBaseModel",
      qualifiedType = "com.super.BaseModel",
      description = Some("A cat model"),
      modelType = Some("DISCRIMINATOR"),
      properties = properties
    )

    write(model) should be ("""{"id":"AnimalBaseModel","name":"AnimalBaseModel","properties":{"id":{"type":"long","required":false}},"description":"A cat model","type":"DISCRIMINATOR"}""")
  }
}

@RunWith(classOf[JUnitRunner])
class ModelInhertenceUtilTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  val json = """{"id":"CatModel","name":"CatModel","qualifiedType":"com.super.CatModel","properties":{"name":{"type":"string","required":false},"id":{"type":"long","required":false}},"description":"A cat model","extends":"model.AnimalBaseModel"}"""
  val model = parse(json).extract[Model]

  val models = ModelInheritenceUtil.expand(Map(model.name -> model))
  models.size should be (2)
  val cat = models("CatModel")
  cat.baseModel should be(Some("AnimalBaseModel"))
  cat.properties.size should be (1)
  cat.modelType should be (None)

  val name = cat.properties.head._2
  name.`type` should be ("string")

  val base = models("AnimalBaseModel")
  base.modelType should be (Some("DISCRIMINATOR"))
  base.properties.size should be (1)

  val id = base.properties.head._2
  id.`type` should be ("long")
}

case class AnimalBaseModel (id: Long)