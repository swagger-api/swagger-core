package com.wordnik.test.swagger.core

import com.wordnik.swagger.core.ApiPropertiesReader
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import javax.xml.bind._
import scala.reflect.BeanProperty
import javax.xml.bind.annotation._
import java.io.ByteArrayOutputStream

@RunWith(classOf[JUnitRunner])
class SpecReaderTest extends FlatSpec with ShouldMatchers {
  it should "read a SimplePojo" in {
    var docObj = ApiPropertiesReader.read(classOf[SimplePojo])
    assert(null != docObj.getName)
  }

  it should "read a ScalaPojo" in {
    var docObj = ApiPropertiesReader.read(classOf[ScalaPojo])
    assert(null != docObj.getName)
  }

  it should "read a ScalaCaseClass" in {
    var docObj = ApiPropertiesReader.read(classOf[ScalaCaseClass])
    assert(null != docObj.getName)
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
  }

  it should "serialize a ScalaPojo" in {
    val ctx = JAXBContext.newInstance(classOf[ScalaPojo]);
    var m = ctx.createMarshaller()
    val e = new ScalaPojo
    e.testInt = 5
    val baos = new ByteArrayOutputStream
    m.marshal(e, baos)
  }

  it should "serialize a ScalaCaseClass" in {
    val ctx = JAXBContext.newInstance(classOf[ScalaCaseClass]);
    var m = ctx.createMarshaller()
    val e =  new ScalaCaseClass
    e.testInt = 5
    val baos = new ByteArrayOutputStream
    m.marshal(e, baos)
  }
}

@XmlRootElement(name = "simplePojo")
class SimplePojo {
  private var te: Int = 1

  @XmlElement(name = "testInt")
  def getTestInt: Int = te
  def setTestInt(te: Int) = { this.te = te }
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
}