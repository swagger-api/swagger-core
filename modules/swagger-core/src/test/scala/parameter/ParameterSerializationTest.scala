import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.models.parameters._

import com.wordnik.swagger.util.Json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.core.JsonGenerator.Feature
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.annotation._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ParameterSerializationTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  val yaml = new ObjectMapper(new YAMLFactory())
  yaml.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  yaml.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  yaml.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  it should "serialize a QueryParameter" in {
    val p = new QueryParameter().property(new StringProperty())
    m.writeValueAsString(p) should be ("""{"in":"query","required":false,"type":"string"}""")
  }

  it should "deserialize a QueryParameter" in {
    val json = """{"in":"query","required":false,"type":"string"}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize a with array QueryParameter" in {
    val p = new QueryParameter()
      .array(true)
      .items(new StringProperty())
      .collectionFormat("multi")
    m.writeValueAsString(p) should be ("""{"in":"query","required":false,"type":"array","items":{"type":"string"},"collectionFormat":"multi"}""")
  }

  it should "deserialize a array QueryParameter" in {
    val json = """{"in":"query","required":false,"type":"array","items":{"type":"string"},"collectionFormat":"multi"}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize a PathParameter" in {
    val p = new PathParameter().property(new StringProperty())
    m.writeValueAsString(p) should be ("""{"in":"path","required":true,"type":"string"}""")
  }

  it should "deserialize a PathParameter" in {
    val json = """{"in":"query","required":true,"type":"string"}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize a with string array PathParameter" in {
    val p = new PathParameter()
      .array(true)
      .items(new StringProperty())
      .collectionFormat("multi")
    m.writeValueAsString(p) should be ("""{"in":"path","required":true,"type":"array","items":{"type":"string"},"collectionFormat":"multi"}""")
    yaml.writeValueAsString(p) should equal (
"--- !<path>\nrequired: true\ntype: \"array\"\nitems:\n  type: \"string\"\ncollectionFormat: \"multi\"\n"
    )
  }

  it should "deserialize a string array PathParameter" in {
    val json = """{"in":"path","required":true,"type":"array","items":{"type":"string"},"collectionFormat":"multi"}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize a with integer array PathParameter" in {
    val p = new PathParameter()
      .array(true)
      .items(new IntegerProperty())
      .collectionFormat("multi")
    m.writeValueAsString(p) should be ("""{"in":"path","required":true,"type":"array","items":{"type":"integer","format":"int32"},"collectionFormat":"multi"}""")
  }

  it should "deserialize a integer array PathParameter" in {
    val json = """{"in":"path","required":true,"type":"array","items":{"type":"integer","format":"int32"},"collectionFormat":"multi"}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize a HeaderParameter" in {
    val p = new HeaderParameter().property(new StringProperty())
    m.writeValueAsString(p) should be ("""{"in":"header","required":true,"type":"string"}""")
    yaml.writeValueAsString(p) should equal(
"--- !<header>\nrequired: true\ntype: \"string\"\n"
    )
  }

  it should "deserialize a HeaderParameter" in {
    val json = """{"in":"header","required":true,"type":"string"}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize a string array HeaderParameter" in {
    val p = new HeaderParameter()
      .array(true)
      .property(new StringProperty())
      .collectionFormat("multi")
    m.writeValueAsString(p) should be ("""{"in":"header","required":true,"type":"string","collectionFormat":"multi"}""")
  }

  it should "deserialize a string array HeaderParameter" in {
    val json = """{"in":"header","required":true,"type":"string","collectionFormat":"multi"}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize a BodyParameter" in {
    val model = new ModelImpl()
      .name("Cat")
      .property("name", new StringProperty())
    val p = new BodyParameter().schema(model)
    m.writeValueAsString(p) should be ("""{"in":"body","required":false,"schema":{"properties":{"name":{"type":"string"}}}}""")
  }

  it should "serialize a BodyParameter to yaml" in {
    val model = new ModelImpl()
      .name("Cat")
      .property("name", new StringProperty())
    val p = new BodyParameter().schema(model)
    yaml.writeValueAsString(p) should equal(
"--- !<body>\nrequired: false\nschema:\n  properties:\n    name:\n      type: \"string\"\n"
    )
  }

  it should "deserialize a BodyParameter" in {
    val json = """{"in":"body","required":false,"schema":{"properties":{"name":{"type":"string"}}}}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }

  it should "serialize an array BodyParameter" in {
    val model = new RefModel("Cat")
    val p = new BodyParameter().schema(model)
    m.writeValueAsString(p) should be ("""{"in":"body","required":false,"schema":{"$ref":"Cat"}}""")
  }

  it should "deserialize an array BodyParameter" in {
    val json = """{"in":"body","required":false,"schema":{"$ref":"Cat"}}"""
    val p = m.readValue(json, classOf[Parameter])
    m.writeValueAsString(p) should equal (json)
  }
}