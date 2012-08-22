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

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util._

import javax.xml.bind._
import javax.xml.bind.annotation._
import java.io.ByteArrayOutputStream

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.JavaConversions._
import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class DocumentationSerializationTest extends FlatSpec with ShouldMatchers {
  behavior of "documentation"
  
  it should "serialize to json" in {
    val doc = getDoc
    val json = JsonUtil.getJsonMapper.writeValueAsString(doc)
    val um = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    val json2 = JsonUtil.getJsonMapper.writeValueAsString(um)
    assert(json === json2)
    assert(um.swaggerVersion === SwaggerSpec.version)
  }
  
  it should "serialize to xml" in {
    val doc = getDoc
    val ctx = JAXBContext.newInstance(classOf[Documentation])
    var m = ctx.createMarshaller()
    
    val baos = new ByteArrayOutputStream
    m.marshal(doc, baos)
    
    println(baos.toString)
  }
  
  def getDoc = {
    val doc = new Documentation("api-12345",
      SwaggerSpec.version,
      "http://www.foo.com/api",
      "/myresource")
    val api = new DocumentationEndPoint("store", "a test endpoint")
    val operation = new DocumentationOperation("GET",
      "gets an item from a store",
      "returns just one")
    api.addOperation(operation)
    doc.addApi(api)
    doc
  }
}