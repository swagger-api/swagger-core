/**
 *  Copyright 2012 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.test.swagger.core

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.annotations.ApiProperty
import com.wordnik.swagger.core.ApiPropertiesReader

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.xml.bind._
import javax.xml.bind.annotation._
import java.io.{ ByteArrayOutputStream, ByteArrayInputStream }

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.reflect.BeanProperty
import scala.annotation.target.field

@RunWith(classOf[JUnitRunner])
class SpecReaderTest extends FlatSpec with ShouldMatchers {
  it should "read a SimplePojo" in {
    var docObj = ApiPropertiesReader.read(classOf[SimplePojo])
    assert((docObj.getFields.map(f => f.name).toSet & Set("testInt", "testString")).size === 2)
  }

  it should "read a ScalaPojo" in {
    var docObj = ApiPropertiesReader.read(classOf[ScalaPojo])
    assert((docObj.getFields.map(f => f.name).toSet & Set("testInt")).size === 1)
  }

  it should "read a ScalaCaseClass" in {
    var docObj = ApiPropertiesReader.read(classOf[ScalaCaseClass])
    assert((docObj.getFields.map(f => f.name).toSet & Set("testInt")).size === 1)
  }

  it should "read a SimplePojo with XMLElement variations" in {
    var docObj = ApiPropertiesReader.read(classOf[SimplePojo2])
    assert((docObj.getFields.map(f => f.name).toSet & Set("testInt", "testString")).size === 2)
  }

  it should "read collection of collection properties " in {
    var docObj = ApiPropertiesReader.read(classOf[TestCollectionOfCollections])
    assert(docObj.getFields.filter(f => f.name == "mapOfMaps").size > 0)
    assert(docObj.getFields.filter(f => f.name == "mapOfMaps").get(0).getParamType() === "Map[string,Map[string,double]]")
    assert(docObj.getFields.filter(f => f.name == "listOfLists").get(0).getParamType() === "List[List[string]]")
    assert(docObj.getFields.filter(f => f.name == "listOfMaps").get(0).getParamType() === "List[Map[string,double]]")
    assert(docObj.getFields.filter(f => f.name == "setofLists").get(0).getParamType() === "Set[List[string]]")
  }

  it should "read scala enum properties as string" in {
    var docObj = ApiPropertiesReader.read(classOf[TestClassWithScalaEnums])
    assert(docObj.getFields.filter(f => f.name == "label").get(0).getParamType() === "String")
  }

  it should "read different data types properly " in {
    var docObj = ApiPropertiesReader.read(classOf[SampleDataTypes])
    var assertedFields = 0;
    for (field <- docObj.getFields) {
      field.name match {
        case "sampleByte" => assert(field.paramType === "byte"); assertedFields += 1;
        case "sampleArrayByte" => assert(field.paramType === "Array[byte]"); assertedFields += 1;
        case "sampleListString" => assert(field.paramType === "Array[String]"); assertedFields += 1;
        case _ =>
      }
    }
    assert(assertedFields === 3)
  }

  it should "read objects and its super class properties" in {
    var docObj = ApiPropertiesReader.read(classOf[ExtendedClass])
    var assertedFields = 0;
    for (field <- docObj.getFields) {
      field.name match {
        case "stringProperty" => assert(field.paramType === "string"); assertedFields += 1;
        case "intProperty" => assert(field.paramType === "int"); assertedFields += 1;
        case "label" => assert(field.paramType === "string"); assertedFields += 1;
        case "transientFieldSerializedGetter" => assert(field.paramType === "string"); assertedFields += 1;
        case _ =>
      }
    }
    assert(assertedFields === 4)

  }

  it should "not create any model properties to default methods like get class " in {
    var docObj = ApiPropertiesReader.read(classOf[ExtendedClass])
    var assertedFields = 0;
    for (field <- docObj.getFields) {
      field.name match {
        case "class" => assert(false, "should not have class property in model object");
        case _ =>
      }
    }
  }

  it should "only read properties with XMLElement annotation if model object has XmlAccessType type NONE annotation " in {
    var docObj = ApiPropertiesReader.read(classOf[ObjectWithNoneAnnotationAndNoElementAnnotations])
    assert(null == docObj.getFields)

    docObj = ApiPropertiesReader.read(classOf[ScalaCaseClass])
    assert(docObj.getFields.size() === 1)

  }

  it should "read properties if attribute is defined as transient in the main class and xml element in the base class " in {
    var docObj = ApiPropertiesReader.read(classOf[ObjectWithTransientGetterAndXMLElementInTrait])
    assert(docObj.getFields.size() === 1)
  }

  it should "read objects inside an array " in {
    var classes: java.util.List[String] = new java.util.ArrayList[String]()
    classes.add(classOf[TestClassWithArrayOfNonPrimitiveObjects].getName);
    val types = TypeUtil.getReferencedClasses(classes)
    assert(types.size() === 2)
  }

  it should "read objects inside a map " in {
    var classes: java.util.List[String] = new java.util.ArrayList[String]()
    classes.add(classOf[ObjectWithChildObjectsInMap].getName);
    val types = TypeUtil.getReferencedClasses(classes)
    assert(types.size() === 2)
  }

  it should "read properties from constructor args" in {
    var docObj = ApiPropertiesReader.read(classOf[TestClassWithConstructorProperties])
    assert(null != docObj.getFields, "should add fields from constructor")
    assert(docObj.getFields.size() === 1)
  }

  it should "read objects with objects form different element and property names" in {
    var classes: java.util.List[String] = new java.util.ArrayList[String]()
    classes.add(classOf[ObjectWithDifferentElementAndPropertyName].getName);
    val types = TypeUtil.getReferencedClasses(classes)
    assert(types.size() === 2)
  }

  it should "read properties with XML attribute annotations" in {
    var docObj = ApiPropertiesReader.read(classOf[ObjectWithRootElementName])
    expect(3) {
      docObj.getFields.size()
    }
  }

  it should "read properties for scala case classes " in {
    var docObj = ApiPropertiesReader.read(classOf[ScalaCaseClassWithScalaSupportedType])
    expect(11) {
      docObj.getFields.size()
    }
  }

  it should "read objects with objects form scala option properties" in {
    var classes: java.util.List[String] = new java.util.ArrayList[String]()
    classes.add(classOf[ScalaCaseClassWithScalaSupportedType].getName);
    val types = TypeUtil.getReferencedClasses(classes)
    assert(types.size() === 3)
  }

  it should "read objects from base class for identifying model classes " in {
    var classes: java.util.List[String] = new java.util.ArrayList[String]()
    classes.add(classOf[ClassToTestModelClassesFromBaseClass].getName);
    val types = TypeUtil.getReferencedClasses(classes)
    assert(types.size() === 2)
  }
}

@RunWith(classOf[JUnitRunner])
class JaxbSerializationTest extends FlatSpec with ShouldMatchers {
  it should "serialize a SimplePojo" in {
    val ctx = JAXBContext.newInstance(classOf[SimplePojo]);
    var m = ctx.createMarshaller()
    val e = new SimplePojo
    e.setTestInt(5)
    val baos = new ByteArrayOutputStream
    m.marshal(e, baos)
    assert(baos.toString == """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><simplePojo><testInt>5</testInt></simplePojo>""")
  }

  it should "deserialize a SimplePojo" in {
    val ctx = JAXBContext.newInstance(classOf[SimplePojo]);
    var u = ctx.createUnmarshaller()
    val b = new ByteArrayInputStream("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?><simplePojo><testInt>5</testInt></simplePojo>""".getBytes)
    val p = u.unmarshal(b).asInstanceOf[SimplePojo]
    assert(p.getTestInt == 5)
  }

  it should "serialize a ScalaPojo" in {
    val ctx = JAXBContext.newInstance(classOf[ScalaPojo]);
    var m = ctx.createMarshaller()
    val e = new ScalaPojo
    e.testInt = 5
    val baos = new ByteArrayOutputStream
    m.marshal(e, baos)
  }

  it should "deserialize a ScalaPojo" in {
    val ctx = JAXBContext.newInstance(classOf[ScalaPojo]);
    var u = ctx.createUnmarshaller()
    val b = new ByteArrayInputStream("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?><scalaishPojo><testInt>5</testInt></scalaishPojo>""".getBytes)
    val p = u.unmarshal(b).asInstanceOf[ScalaPojo]
    assert(p.testInt == 5)
  }

  it should "serialize a ScalaCaseClass" in {
    val ctx = JAXBContext.newInstance(classOf[ScalaCaseClass]);
    var m = ctx.createMarshaller()
    val e = new ScalaCaseClass
    e.testInt = 5
    val baos = new ByteArrayOutputStream
    m.marshal(e, baos)
  }

  it should "deserialize a ScalaCaseClass" in {
    val ctx = JAXBContext.newInstance(classOf[ScalaCaseClass]);
    var u = ctx.createUnmarshaller()
    val b = new ByteArrayInputStream("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?><scalaCaseClass><testInt>5</testInt></scalaCaseClass>""".getBytes)
    val p = u.unmarshal(b).asInstanceOf[ScalaCaseClass]
    assert(p.testInt == 5)
  }

  it should "serialize a ExtendedClass" in {
    val ctx = JAXBContext.newInstance(classOf[ExtendedClass]);
    var m = ctx.createMarshaller()
    val e = new ExtendedClass
    e.setTransientFieldSerializedGetter("Field1")
    e.setLabel("Field2")
    e.setStringProperty("Field3")
    m.marshal(e, System.out)
  }

  it should "serialize a TestObjectForNoneAnnotation with no xml element annotations" in {
    val ctx = JAXBContext.newInstance(classOf[ObjectWithNoneAnnotationAndNoElementAnnotations]);
    var m = ctx.createMarshaller()
    val e = new ObjectWithNoneAnnotationAndNoElementAnnotations
    e.setTestString("test String")
    m.marshal(e, System.out)
  }
}

@RunWith(classOf[JUnitRunner])
class JsonSerializationTest extends FlatSpec with ShouldMatchers {
  it should "serialize a SimplePojo" in {
    val mapper = JsonUtil.getJsonMapper
    val e = new SimplePojo
    e.setTestInt(5)
    mapper.writeValueAsString(e)
  }

  it should "serialize a ScalaPojo" in {
    val mapper = JsonUtil.getJsonMapper
    val e = new ScalaPojo
    e.testInt = 5
    mapper.writeValueAsString(e)
  }

  it should "serialize a ScalaCaseClass" in {
    val mapper = JsonUtil.getJsonMapper
    val e = new ScalaCaseClass
    e.testInt = 5
    mapper.writeValueAsString(e)
  }
}

@XmlRootElement(name = "simplePojo")
class SimplePojo {
  private var te: Int = 1
  private var testString: String = _

  @XmlElement(name = "testInt")
  def getTestInt: Int = te
  def setTestInt(te: Int) = { this.te = te }

  @XmlElement(name = "testString")
  def getTestString: String = testString
  def setTestString(testString: String) = { this.testString = testString }
}

@XmlRootElement(name = "simplePojo2")
@XmlAccessorType(XmlAccessType.NONE)
class SimplePojo2 {
  @XmlElement(name = "testInt")
  var te: Int = 1

  @XmlElement(name = "testString")
  var ts: String = _
}

@XmlRootElement(name = "scalaishPojo")
@XmlAccessorType(XmlAccessType.NONE)
class ScalaPojo {
  @XmlElement(name = "testInt")
  @BeanProperty
  var testInt = 0
}

@XmlRootElement(name = "scalaCaseClass")
@XmlAccessorType(XmlAccessType.NONE)
case class ScalaCaseClass() {
  @XmlElement(name = "testInt")
  @BeanProperty
  var testInt = 0

  @JsonIgnore
  @XmlTransient
  @BeanProperty
  var testTransient: List[String] = _
}

@XmlRootElement(name = "BaseClass")
class BaseClass {
  @BeanProperty var stringProperty: String = _
  @BeanProperty var intProperty: Int = _

  @XmlTransient
  var label: String = _

  def setLabel(label: String) =
    this.label = label

  @XmlElement(name = "label")
  def getLabel() = label
}

@XmlRootElement(name = "ExtendedClass")
class ExtendedClass extends BaseClass {
  @BeanProperty var floatProperty: Float = _
  @BeanProperty var longProperty: Long = _

  @XmlTransient
  var transientFieldSerializedGetter: String = _

  def setTransientFieldSerializedGetter(value: String) =
    this.transientFieldSerializedGetter = value

  @XmlElement(name = "transientFieldSerializedGetter")
  def getTransientFieldSerializedGetter() = transientFieldSerializedGetter
}

@XmlRootElement(name = "sampleDataTypes")
class SampleDataTypes {
  @BeanProperty var sampleByte: Byte = _
  @BeanProperty var sampleArrayByte: Array[Byte] = _
  @BeanProperty var sampleArrayString: Array[String] = _
  @BeanProperty var sampleListString: Array[String] = _

}

@XmlRootElement(name = "ObjectWithNoneAnnotationAndNoElementAnnotations")
@XmlAccessorType(XmlAccessType.NONE)
case class ObjectWithNoneAnnotationAndNoElementAnnotations() {
  @BeanProperty
  var testInt = 0

  @BeanProperty
  var testString: String = _
}

@XmlAccessorType(XmlAccessType.NONE)
trait Id {
  @XmlElement @BeanProperty var id: String = _
}

@XmlRootElement(name = "ObjectWithTransientGetterAndXMLElementInTrait")
@XmlAccessorType(XmlAccessType.NONE)
class ObjectWithTransientGetterAndXMLElementInTrait extends Id {
  @XmlTransient
  override def getId(): String = super.getId()
}

@XmlRootElement(name = "TestCollectionOfCollections")
@XmlAccessorType(XmlAccessType.NONE)
class TestCollectionOfCollections {
  @XmlElement @BeanProperty var mapOfMaps: java.util.HashMap[String, java.util.HashMap[String, java.lang.Double]] = _
  @XmlElement @BeanProperty var listOfLists: java.util.List[java.util.List[String]] = _
  @XmlElement @BeanProperty var listOfMaps: java.util.List[java.util.HashMap[String, java.lang.Double]] = _
  @XmlElement @BeanProperty var setofLists: java.util.Set[java.util.List[String]] = _
  //the folowing use case is no currently supported by swagger
  //@XmlElement @BeanProperty var arrayofLists: Array[java.util.List[String]] = _
}

@XmlRootElement(name = "TestClassWithScalaEnums")
class TestClassWithScalaEnums {
  @ApiProperty(dataType = "String") @XmlElement @BeanProperty var label: ScalaEnums.Value = _
}

@XmlRootElement(name = "TestClassWithJavaEnums")
class TestClassWithJavaEnums {

  @ApiProperty(dataType = "String") @XmlElement(name = "enumType") @BeanProperty var enumType: TestEnum = TestEnum.PUBLIC
  @XmlElement @BeanProperty var name: String = _
}

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TestClassWithArrayOfNonPrimitiveObjects")
class TestClassWithArrayOfNonPrimitiveObjects {
  @XmlElement @BeanProperty var arrayWithObjects: Array[BaseClass] = _
}

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TestClassWithConstructorProperties")
class TestClassWithConstructorProperties(@(XmlElement @field)(name = "text")@BeanProperty var text: String) {
}

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ObjectWithDifferentRootElementName")
class ObjectWithRootElementName {
  @XmlAttribute(name = "label") @BeanProperty var label: String = _
  @XmlAttribute(name = "width") @BeanProperty var width: Int = _
  @XmlAttribute(name = "height") @BeanProperty var height: Int = _
}

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ObjectWithDifferentElementAndPropertyName")
class ObjectWithDifferentElementAndPropertyName {
  @XmlElement(name = "differentElementAndPropertyName") @BeanProperty var sizes: java.util.List[ObjectWithRootElementName] = new java.util.ArrayList[ObjectWithRootElementName]
}

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ObjectWithChildObjectsInMap")
class ObjectWithChildObjectsInMap {
  @XmlElement @BeanProperty var objectsInMap: java.util.Map[String, ObjectWithRootElementName] = _
}

object ScalaEnums extends Enumeration {
  type ScalaEnums = Value

  val Abbreviation = Value("abbrev")
  val AdjectivalComplement = Value("acomp")
  val AdverbialClauseModifier = Value("advcl")
}

case class ScalaCaseClassWithScalaSupportedType(
  intType: Int,
  longType: Long,
  stringType: String,
  dateType: java.util.Date,
  mapType: Map[String, Seq[ObjectWithRootElementName]],
  optionType: Option[TestClassWithConstructorProperties],
  seqType: Seq[String],
  setType: Set[String],
  seqOfTuples: Seq[(String, Double)],
  @(ApiProperty @field)(dataType = "String") enumType: ScalaEnums.Value,
  collectionOfCollections: Map[String, Seq[ObjectWithRootElementName]]) {
}

class ClassToTestModelClassesFromBaseClass extends ObjectWithChildObjectsInMap {
}
