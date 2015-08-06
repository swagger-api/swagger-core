import javax.xml.bind.annotation._

import io.swagger.converter.ModelConverters
import io.swagger.models.ModelImpl
import io.swagger.util.Json
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.beans.BeanProperty
import scala.collection.JavaConverters.mapAsScalaMapConverter

@RunWith(classOf[JUnitRunner])
class XmlModelTest extends FlatSpec with Matchers {
  it should "process an XML model attribute" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Monster])
    val model = schemas.get("Monster")

    model should not be (null)
    model.isInstanceOf[ModelImpl] should be(true)
    var xml = model.asInstanceOf[ModelImpl].getXml()

    xml should not be (null)
    xml.getName() should equal("monster")
    val property = model.getProperties().get("children")
    property should not be (null)
    xml = property.getXml()
    xml.getWrapped should equal(true)
    xml.getName() should be (null)
  }

  it should "not create an xml object" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Address])
    val model = schemas.get("Address")

    model should not be (null)
    model.isInstanceOf[ModelImpl] should be(true)

    val property = model.getProperties().get("streetNumber")
    var xml = property.getXml()

    xml should be(null)
  }

  it should "stay hidden per 534" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[Issue534])
    schemas.get("Issue534").getProperties().size() should be(1)
  }

  it should "process a model with JAXB annotations" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[ModelWithJAXBAnnotations])
    schemas.size should be (1)
    val model = schemas.get("ModelWithJAXBAnnotations")
    model should not be (null)
    model.isInstanceOf[ModelImpl] should be (true)
    var rootXml = model.asInstanceOf[ModelImpl].getXml()
    rootXml should not be (null)
    rootXml.getName() should equal("rootName")

    for ((name, property) <- model.getProperties.asScala) {
      name match {
        case "id" =>
          var xml = property.getXml
          xml should not be (null)
          xml.getName should be (null)
          xml.getAttribute should equal (true)
          xml.getWrapped should be (null)
        case "name" =>
          var xml = property.getXml
          xml should not be (null)
          xml.getName should be ("renamed")
          xml.getAttribute should be (null)
          xml.getWrapped should be (null)
        case "list" | "forcedElement" =>
          property.getXml should be (null)
        case "wrappedList" =>
          var xml = property.getXml
          xml should not be (null)
          xml.getName should be ("wrappedListItems")
          xml.getAttribute should be (null)
          xml.getWrapped should equal (true)
        case _ =>
          fail(s"""Property "${name}" was not expected""")
      }
    }
  }

  it should "deserialize a model" in {
    val yaml = """
---
type: "object"
properties:
  id:
    type: "string"
    xml:
      attribute: true
  name:
    type: "string"
    xml:
      name: "renamed"
  list:
    type: "array"
    items:
      type: "string"
  wrappedList:
    type: "array"
    xml:
      name: "wrappedListItems"
      wrapped: true
    items:
      type: "string"
  forcedElement:
    type: "array"
    items:
      type: "string"
xml:
  name: "rootName"
    """
    val model = io.swagger.util.Yaml.mapper().readValue(yaml, classOf[ModelImpl])

    val wrappedList = model.getProperties.get("wrappedList")
    wrappedList should not be (null)
    wrappedList.getXml() should not be (null)
    wrappedList.getXml().getName() should be ("wrappedListItems")
  }
}

@XmlRootElement(name = "monster")
class Monster {
  @BeanProperty
  var name: String = _

  @XmlElementWrapper()
  @XmlElement(name = "children")
  @BeanProperty
  var children: java.util.List[String] = _
}
