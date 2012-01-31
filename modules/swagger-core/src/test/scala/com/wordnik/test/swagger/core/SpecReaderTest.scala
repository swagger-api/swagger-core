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
import com.wordnik.swagger.core.ApiPropertiesReader

import javax.xml.bind._
import javax.xml.bind.annotation._
import java.io.{ ByteArrayOutputStream, ByteArrayInputStream }

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.JavaConversions._
import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class SpecReaderTest extends FlatSpec with ShouldMatchers {
  it should "read a SimplePojo" in {
    var docObj = ApiPropertiesReader.read(classOf[SimplePojo])
    assert((docObj.getFields.map(f=>f.name).toSet & Set("testInt","testString")).size === 2)
  }

  it should "read a ScalaPojo" in {
    var docObj = ApiPropertiesReader.read(classOf[ScalaPojo])
    assert((docObj.getFields.map(f=>f.name).toSet & Set("testInt")).size === 1)
  }

  it should "read a ScalaCaseClass" in {
    var docObj = ApiPropertiesReader.read(classOf[ScalaCaseClass])
    assert((docObj.getFields.map(f=>f.name).toSet & Set("testInt")).size === 1)
  }

  it should "read a SimplePojo with XMLElement variations" in {
    var docObj = ApiPropertiesReader.read(classOf[SimplePojo2])
    assert((docObj.getFields.map(f=>f.name).toSet & Set("testInt","testString")).size === 2)
  }

  it should "read different data types properly " in {
    var docObj = ApiPropertiesReader.read(classOf[SampleDataTypes])
    var assertedFields = 0;
    for(field <- docObj.getFields){
      field.name match {
        case "sampleByte" => assert(field.paramType === "byte"); assertedFields += 1;
        case "sampleArrayByte" => assert(field.paramType === "Array[byte]"); assertedFields += 1;
        case "sampleListString" => assert(field.paramType === "Array[java.lang.String]"); assertedFields += 1;
        case _ =>
      }
    }
    assert(assertedFields === 3)
  }

  it should "read objects and its super class properties" in {
    var docObj = ApiPropertiesReader.read(classOf[ExtendedClass])
    var assertedFields = 0;
    for(field <- docObj.getFields){
      field.name match {
        case "stringProperty" => assert(field.paramType === "string");assertedFields += 1;
        case "intProperty" => assert(field.paramType === "int");assertedFields += 1;
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
    for(field <- docObj.getFields){
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
  @XmlElement(name = "testInt") @BeanProperty
  var testInt = 0
}

@XmlRootElement(name = "scalaCaseClass")
@XmlAccessorType(XmlAccessType.NONE)
case class ScalaCaseClass() {
  @XmlElement(name = "testInt") @BeanProperty
  var testInt = 0

  @XmlTransient @BeanProperty
  var testTransient: List[String] = _
}

@XmlRootElement(name= "BaseClass")
class BaseClass {
  @BeanProperty var stringProperty:String = _
  @BeanProperty var intProperty:Int = _

  @XmlTransient
  var label:String =_

  def setLabel(label: String) =
    this.label = label

  @XmlElement(name = "label")
  def getLabel() = label
}

@XmlRootElement(name= "ExtendedClass")
class ExtendedClass extends BaseClass{
  @BeanProperty var floatProperty:Float = _
  @BeanProperty var longProperty:Long = _

  @XmlTransient
  var transientFieldSerializedGetter:String =_

  def setTransientFieldSerializedGetter(value: String) =
    this.transientFieldSerializedGetter = value

  @XmlElement(name = "transientFieldSerializedGetter")
  def getTransientFieldSerializedGetter() = transientFieldSerializedGetter
}

@XmlRootElement(name= "sampleDataTypes")
class SampleDataTypes {
  @BeanProperty var sampleByte:Byte = _
  @BeanProperty var sampleArrayByte:Array[Byte] = _
  @BeanProperty var sampleArrayString:Array[String] = _
  @BeanProperty var sampleListString:Array[String] = _

}

@XmlRootElement(name = "ObjectWithNoneAnnotationAndNoElementAnnotations")
@XmlAccessorType(XmlAccessType.NONE)
case class ObjectWithNoneAnnotationAndNoElementAnnotations() {
  @BeanProperty
  var testInt = 0

  @BeanProperty
  var testString:String = _
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

