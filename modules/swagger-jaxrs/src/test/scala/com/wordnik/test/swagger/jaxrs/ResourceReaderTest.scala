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

package com.wordnik.test.swagger.jaxrs

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.jaxrs._

import com.wordnik.test.swagger.core.testdata._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.JavaConversions._
import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class ResourceReaderTest extends FlatSpec with ShouldMatchers {
  "ApiPropertiesReader" should "read a SimplePojo" in {
    var docObj = ApiPropertiesReader.read(classOf[SampleOutput])
    assert(docObj != null)
    assert((docObj.getFields.map(f => f.name).toSet & Set("theName", "theValue")).size === 2)
    assert(docObj.getFields.filter(f => f.name == "id")(0).required == true)
  }

  behavior of "ApiReader"

  it should "read a simple resource class" in {
    val helpApi = new HelpApi
    val doc = helpApi.filterDocs(JaxrsApiReader.read(classOf[BasicResourceJSON], "1.123", "2.345", "http://my.host.com/basepath", "/sample"),
      null,
      null,
      null,
      null)
      
    println(com.wordnik.swagger.core.util.JsonUtil.getJsonMapper.writeValueAsString(doc))
      
    assert(doc.apiVersion == "1.123")
    assert(doc.swaggerVersion == "2.345")
    assert(doc.basePath == "http://my.host.com/basepath")
    assert(doc.resourcePath == "/sample")
    assert(doc.getApis.size === 3)
    assert(doc.getModels.size === 2)
    
    // verify the "howdy" model
    val props = doc.getModels().get("Howdy").properties.toMap
    assert((props.map(key=>key._1).toSet & Set("id", "theName", "theValue")).size == 3)
  }

  it should "read a resource class from a listing path" in {
    // simulate loading from the listing class
    val loadingClass = classOf[RemappedResourceJSON]
    val helpApi = new HelpApi
    val doc = helpApi.filterDocs(JaxrsApiReader.read(loadingClass, "1.123", "2.345", "http://my.host.com/basepath", "/sample"),
      null,
      null,
      null,
      null)
    assert(doc.apiVersion === "1.123")
    assert(doc.swaggerVersion === "2.345")
    assert(doc.basePath === "http://my.host.com/basepath")
    assert(doc.resourcePath === "/sample")
    assert(doc.getApis.size === 3)
    assert(doc.getModels.size === 2)

    val props = doc.getModels().get("Howdy").properties.toMap
    assert((props.map(key=>key._1).toSet & Set("id", "theName", "theValue")).size == 3)
    val inputprops = doc.getModels().get("SampleInput").properties.toMap
    assert((inputprops.map(key=>key._1).toSet & Set("name", "value")).size === 2)

  }

  it should "NOT read a resource class from a listing path" in {
    // simulate loading from the listing class
    val loadingClass = classOf[RemappedResourceListingJSON]
    val helpApi = new HelpApi
    val doc = helpApi.filterDocs(JaxrsApiReader.read(loadingClass, "1.123", "2.345", "http://my.host.com/basepath", "/sample"),
      null,
      null,
      null,
      null)
    assert(doc.apiVersion === "1.123")
    assert(doc.swaggerVersion === "2.345")
    assert(doc.basePath === "http://my.host.com/basepath")
    assert(doc.resourcePath === "/sample")
    assert(doc.getApis === null)
  }

  it should "Create a collection response for primitive types" in {
    val loadingClass = classOf[RemappedResourceJSON]
    val helpApi = new HelpApi
    val doc = helpApi.filterDocs(JaxrsApiReader.read(loadingClass, "1.123", "2.345", "http://my.host.com/basepath", "/sample"),
      null,
      null,
      null,
      null)
    val api = doc.getApis.filter(a => (a.path == "/basic.{format}/getStringList"))(0)
    val responseclass = api.getOperations().get(0).getResponseClass()
    assert(responseclass === "List[String]")
  }
}
