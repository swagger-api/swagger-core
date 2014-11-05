import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.wordnik.swagger.converter._
import com.wordnik.swagger.models._
import com.wordnik.swagger.util.Json
import models._
import models.composition.Pet
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import rapture.io.{Json => RJson}

import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class ScalaModelTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a simple scala case class" in {
    m.registerModule(DefaultScalaModule)

    val schemas = ModelConverters.read(classOf[SimpleCaseClass])
    m.writeValueAsString(schemas) should equal( """{"SimpleCaseClass":{"properties":{"name":{"type":"string"},"count":{"type":"integer","format":"int32"}}}}""")
  }

  it should "convert a scala case class with List property" in {
    m.registerModule(DefaultScalaModule)

    val schemas = ModelConverters.read(classOf[CaseClassWithList])
    m.writeValueAsString(schemas) should equal( """{"CaseClassWithList":{"properties":{"name":{"type":"string"},"items":{"type":"array","items":{"type":"string"}}}}}""")
  }

  it should "convert a scala case class with optional value" in {
    m.registerModule(DefaultScalaModule)

    val schemas = ModelConverters.read(classOf[CaseClassWithOptionLong])
    val props = (schemas.get("CaseClassWithOptionLong")).asInstanceOf[ModelImpl].getProperties()
    val propertyCount = props.keySet.size

    val keys = props.keySet.asScala.toList
    keys(0) should be("intValue")
    keys(1) should be("longValue")
    keys(2) should be("setValue")
    keys(3) should be("dateValue")
    keys(4) should be("booleanValue")

    m.writeValueAsString(schemas) should equal( """{"CaseClassWithOptionLong":{"properties":{"intValue":{"type":"integer","format":"int32"},"longValue":{"type":"array","items":{"$ref":"Object"}},"setValue":{"type":"array","items":{"type":"string"}},"dateValue":{"type":"string","format":"date-time"},"booleanValue":{"type":"boolean"}}}}""")
  }

  it should "convert a scala case class with nexted models" in {
    m.registerModule(DefaultScalaModule)
    val schemas = ModelConverters.readAll(classOf[NestedModel])

    m.writeValueAsString(schemas) should equal( """{"NestedModel":{"properties":{"complexModel":{"$ref":"ComplexModel"},"localtime":{"type":"string","format":"date-time"}}},"ComplexModel":{"properties":{"name":{"type":"string"},"age":{"type":"integer","format":"int32"}}}}""")
  }

  it should "read an interface" in {
    val schemas = ModelConverters.readAll(classOf[Pet])

    val model: RJson = RJson.parse(Json.pretty(schemas))

    val pet: RJson = model.Pet
    pet.properties.name.`type`.get[String] should equal("string")
    pet.properties.`type`.`type`.get[String] should equal("string")
    pet.properties.isDomestic.`type`.get[String] should equal("boolean")
  }
}
