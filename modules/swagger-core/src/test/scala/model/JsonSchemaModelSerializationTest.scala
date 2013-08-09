package model

import com.wordnik.swagger.model._

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
class JsonSchemaModelSerializationTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "deserialize a model" in {
    val jsonString = """
    {
      "id":"Foo",
      "name":"Bar",
      "properties": {
        "id": {
          "type":"string",
          "required":true,
          "description":"id"
        },
        "name": {
          "type":"string",
          "required":false,
          "description":"name"
        },
        "tags": {
          "type":"array",
          "items": {
            "type":"string"
          }
        }
      },
      "description":"nice model"
    }
    """
    val json = parse(jsonString)
    json.extract[Model] match {
      case model: Model => {
        model.id should be ("Foo")
        model.name should be ("Bar")
        model.properties should not be (null)
        model.properties.size should be (3)
        model.description should be (Some("nice model"))
        model.properties("id") match {
          case e: ModelProperty => {
            e.`type` should be ("string")
            e.required should be (true)
            e.description should be (Some("id"))
          }
          case _ => fail("missing property id")
        }
        model.properties("name") match {
          case e: ModelProperty => {
            e.`type` should be ("string")
            e.required should be (false)
            e.description should be (Some("name"))
          }
          case _ => fail("missing property name")
        }

        model.properties("tags") match {
          case e: ModelProperty => {
            e.`type` should be ("array")
            e.required should be (false)
            e.items match {
              case Some(items) => items.`type` should be ("string")
              case _ => fail("didn't find ref for Array")
            }
          }
          case _ => fail("missing property name")
        }
      }
      case _ => fail("expected type Model")
    }
  }

  it should "serialize a model" in {
    val ref = Model("Foo", "Bar", "com.fun.Bar", (LinkedHashMap("s" -> ModelProperty("string", "java.lang.string", 0, true, Some("a string")))))
    write(ref) should be ("""{"id":"Foo","required":["s"],"properties":{"s":{"type":"string","description":"a string"}}}""")
  }
}

@RunWith(classOf[JUnitRunner])
class JsonSchemaModelRefSerializationTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "deserialize a model ref" in {
    val jsonString = """
    {
      "$ref":"Foo",
      "type":"Bar"
    }
    """
    val json = parse(jsonString)
    json.extract[ModelRef] match {
      case p: ModelRef => {
        p.ref should be (Some("Foo"))
        p.`type` should be ("Bar")
      }
      case _ => fail("expected type ModelRef")
    }
  }

  it should "serialize a model ref" in {
    val ref = ModelRef("Foo", Some("Bar"))
    write(ref) should be ("""{"type":"Foo","$ref":"Bar"}""")
  }
}

@RunWith(classOf[JUnitRunner])
class JsonSchemaModelPropertySerializationTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "deserialize a model property with allowable values and ref" in {
    val jsonString = """
    {
      "type":"string",
      "required":false,
      "description":"nice",
      "enum": ["1","2","3"],
      "items":{
        "type":"Foo",
        "$ref":"Bar"
      }
    }
    """
    val json = parse(jsonString)
    json.extract[ModelProperty] match {
      case p: ModelProperty => {
        p.`type` should be ("string")
        p.required should be (false)
        p.description should be (Some("nice"))
        p.allowableValues match {
          case e: AllowableListValues => e.values should be (List("1","2","3"))
          case _ => fail("expected allowable values")
        }
        p.items match {
          case Some(e: ModelRef) => {
            e.`type` should be ("Foo")
            e.ref should be (Some("Bar"))
          }
          case _ => fail("expected type ModelProperty")
        }
      }
      case _ => fail("expected type ModelProperty")
    }
  }

  it should "serialize a model property with allowable values and ref" in {
    val p = ModelProperty("string", "java.lang.String", 0, false, Some("nice"), AllowableListValues(List("a","b")),Some(ModelRef("Foo",Some("Bar"))))
    write(p) should be ("""{"type":"string","description":"nice","items":{"type":"Foo","$ref":"Bar"},"enum":["a","b"]}""")
  }

  it should "deserialize a model property with allowable values" in {
    val jsonString = """
    {
      "type":"string",
      "required":false,
      "description":"nice",
      "enum": ["1","2","3"]
    }
    """
    val json = parse(jsonString)
    json.extract[ModelProperty] match {
      case p: ModelProperty => {
        p.`type` should be ("string")
        p.required should be (false)
        p.description should be (Some("nice"))
        p.allowableValues match {
          case e: AllowableListValues => e.values should be (List("1","2","3"))
          case _ => fail("expected allowable values")
        }
      }
      case _ => fail("expected type ModelProperty")
    }
  }

  it should "serialize a model property with allowable values" in {
    val p = ModelProperty("string", "java.lang.String", 0, false, Some("nice"), AllowableListValues(List("a","b")))
    write(p) should be ("""{"type":"string","description":"nice","enum":["a","b"]}""")
  }

  it should "deserialize a model property" in {
    val jsonString = """
    {
      "type":"string",
      "required":true,
      "description":"nice"
    }
    """
    val json = parse(jsonString)
    json.extract[ModelProperty] match {
      case p: ModelProperty => {
        p.`type` should be ("string")
        p.required should be (true)
        p.description should be (Some("nice"))
      }
      case _ => fail("expected type ModelProperty")
    }
  }

  it should "serialize a model property" in {
    val p = ModelProperty("string", "java.lang.String", 0, false, Some("nice"))
    write(p) should be ("""{"type":"string","description":"nice"}""")
  }
}

@RunWith(classOf[JUnitRunner])
class JsonSchemaAllowableValuesSerializersTest extends FlatSpec with ShouldMatchers {
  implicit val formats = SwaggerSerializers.formats

  it should "deserialize allowable value list" in {
    val allowableValuesListString = """
    {
      "valueType":"LIST",
      "values":["1","2","3"]
    }
    """
    val json = parse(allowableValuesListString)
    json.extract[AllowableValues] match {
      case avl: AllowableListValues => {
        avl.valueType should be ("LIST")
        avl.values should be (List("1","2","3"))        
      }
    }
  }

  it should "serialize allowable values list" in {
    val l = AllowableListValues(List("1","2","3"))
    write(l) should be ("""{"valueType":"LIST","values":["1","2","3"]}""")
  }

  it should "deserialize allowable values range" in {
    val allowableValuesRangeString = """
    {
      "valueType":"RANGE",
      "min":"abc",
      "max":3
    }
    """
    val json = parse(allowableValuesRangeString)
    json.extract[AllowableValues] match {
      case avr: AllowableRangeValues => {
        avr.min should be ("abc")
        avr.max should be ("3")        
      }
      case _ => fail("wrong type returned, should be AllowabeValuesList")
    }
  }

  it should "serialize allowable values range" in {
    val l = AllowableRangeValues("-1", "3")
    write(l) should be ("""{"valueType":"RANGE","min":"-1","max":"3"}""")
  }
}