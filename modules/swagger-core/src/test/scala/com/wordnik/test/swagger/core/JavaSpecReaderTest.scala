package com.wordnik.test.swagger.core

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import com.wordnik.swagger.core.util.TypeUtil
import com.wordnik.swagger.core.{DocumentationObject, SwaggerContext, ApiPropertiesReader}
import scala.collection.JavaConverters._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * User: ramesh
 * Date: 3/23/12
 * Time: 8:36 AM
 */

@RunWith(classOf[JUnitRunner])
class JavaSpecReaderTest extends FlatSpec with ShouldMatchers {

  behavior of "Java Spec reader"

  it should "not create swagger doc model for java ENUM classes " in {
    var classes:java.util.List[String] = new java.util.ArrayList[String]()
    classes.add(classOf[TestClassWithJavaEnums].getName);
    val types = TypeUtil.getReferencedClasses(classes)
    var docs = new java.util.ArrayList[DocumentationObject]()
    types.asScala.foreach(t => {
      val c = SwaggerContext.loadClass(t)
      val doc = ApiPropertiesReader.read(c)
      if(null != doc){
       docs.add(doc)
      }
    })
    assert(docs.size() === 1, "should only create one document and ignore enums")
  }
}
