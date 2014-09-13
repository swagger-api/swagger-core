import models._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models.ModelImpl
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.converter._

import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ModelConverterTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model" in {
    val schemas = ModelConverters.read(classOf[Person])
    m.writeValueAsString(schemas) should equal ("""{"Person":{"properties":{"id":{"type":"integer","format":"int64"},"firstName":{"type":"string"},"address":{"$ref":"Address"},"properties":{"type":"object","additionalProperties":{"type":"string"}},"birthDate":{"type":"string","format":"date-time"},"float":{"type":"number","format":"float"},"double":{"type":"number","format":"double"}}}}""")
  }

  it should "convert a model with Joda DateTime" in {
    val schemas = ModelConverters.read(classOf[JodaDateTimeModel])
    m.writeValueAsString(schemas) should equal ("""{"JodaDateTimeModel":{"properties":{"createdAt":{"type":"string","format":"date-time"}}}}""")
  }

  ignore should "convert a model with Guava optionals" in {
    m.registerModule(new GuavaModule())

    val schemas = ModelConverters.read(classOf[GuavaModel])
    m.writeValueAsString(schemas) should equal ("""{"GuavaModel":{"properties":{"name":{"type":"string"}}}}""")
  }

  it should "convert a simple scala case class" in {
    m.registerModule(DefaultScalaModule)

    val schemas = ModelConverters.read(classOf[SimpleCaseClass])
    m.writeValueAsString(schemas) should equal ("""{"SimpleCaseClass":{"properties":{"name":{"type":"string"},"count":{"type":"integer","format":"int32"}}}}""")
  }

  it should "convert a scala case class with List property" in {
    m.registerModule(DefaultScalaModule)

    val schemas = ModelConverters.read(classOf[CaseClassWithList])
    m.writeValueAsString(schemas) should equal ("""{"CaseClassWithList":{"properties":{"name":{"type":"string"},"items":{"type":"array","items":{"type":"string"}}}}}""")
  }

  it should "convert a scala case class with optional value" in {
    m.registerModule(DefaultScalaModule)

    val schemas = ModelConverters.read(classOf[CaseClassWithOptionLong])
    val props = (schemas.get("CaseClassWithOptionLong")).asInstanceOf[ModelImpl].getProperties()
    val propertyCount = props.keySet.size

    val keys = props.keySet.asScala.toList
    keys(0) should be ("intValue")
    keys(1) should be ("longValue")
    keys(2) should be ("setValue")
    keys(3) should be ("dateValue")
    keys(4) should be ("booleanValue")

    m.writeValueAsString(schemas) should equal ("""{"CaseClassWithOptionLong":{"properties":{"intValue":{"type":"integer","format":"int32"},"longValue":{"type":"array","items":{"$ref":"Object"}},"setValue":{"type":"array","items":{"type":"string"}},"dateValue":{"type":"string","format":"date-time"},"booleanValue":{"type":"boolean"}}}}""")
  }

  it should "convert a scala case class with nexted models" in {
    m.registerModule(DefaultScalaModule)
    val schemas = ModelConverters.readAll(classOf[NestedModel])

    m.writeValueAsString(schemas) should equal ("""{"NestedModel":{"properties":{"complexModel":{"$ref":"ComplexModel"},"localtime":{"type":"string","format":"date-time"}}},"ComplexModel":{"properties":{"name":{"type":"string"},"age":{"type":"integer","format":"int32"}}}}""")
  }

  it should "convert a map model" in {
    val schemas = ModelConverters.readAll(classOf[java.util.Map[String, String]])
    println(m.writeValueAsString(schemas))
  }
}
