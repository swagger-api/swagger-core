package com.wordnik.test.swagger.core

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import com.wordnik.swagger.core.util.TypeUtil
import com.wordnik.swagger.core.{DocumentationObject, SwaggerContext, ApiPropertiesReader}
import scala.collection.JavaConverters._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class JavaSpecReaderTest extends FlatSpec with ShouldMatchers {

  behavior of "Java Spec reader"

  it should "not create swagger doc model for java ENUM classes " in {
    val classes = List(classOf[TestClassWithJavaEnums].getName)
    val types = TypeUtil.getReferencedClasses(classes)

    var docs = new java.util.ArrayList[DocumentationObject]()
    types.foreach(t => {
      val c = SwaggerContext.loadClass(t)
      val doc = ApiPropertiesReader.read(c.getName())
      if(null != doc){
       docs.add(doc)
      }
    })
    assert(docs.size() === 1, "should only create one document and ignore enums")
  }
}
