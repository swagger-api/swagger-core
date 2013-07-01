/**
 *  Copyright 2013 Wordnik, Inc.
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

package com.wordnik.test.swagger.integration

import com.wordnik.swagger.model._

import com.wordnik.swagger.core.util.ScalaJsonUtil

import org.junit.runner.RunWith

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.JavaConversions._

import scala.io._

@RunWith(classOf[JUnitRunner])
class ResourceListingIT extends FlatSpec with ShouldMatchers {
  it should "read a resource listing" in {
    val json = Source.fromURL("http://localhost:8002/api/api-docs").mkString
    val doc = ScalaJsonUtil.mapper.readValue(json, classOf[ResourceListing])

    assert(doc.apis.size === 2)
    assert((doc.apis.map(api => api.path).toSet & Set("/pet", "/user")).size == 2)
  }

  ignore should "read the resource listing in XML" in {
    val xmlString = Source.fromURL("http://localhost:8002/api/api-docs.xml").mkString
    val xml = scala.xml.XML.loadString(xmlString)
    assert(((xml \ "apis").map(api => (api \ "path").text).toSet & Set("/pet", "/user")).size == 2)
  }

  ignore should "read the pet api description" in {
    val json = Source.fromURL("http://localhost:8002/api/api-docs/pet").mkString
    println(json)
    val doc = ScalaJsonUtil.mapper.readValue(json, classOf[ApiListing])
    assert(doc.apis.size === 3)
    assert((doc.apis.map(api => api.path).toSet &
      Set("/pet/{petId}",
        "/pet/findByStatus",
        "/pet/findByTags")).size == 3)
  }

  ignore should "read the user api with array and list data types as post data" in {
    val json = Source.fromURL("http://localhost:8002/api/api-docs/user").mkString
    val doc = ScalaJsonUtil.mapper.readValue(json, classOf[ApiListing])
    assert(doc.apis.size === 6)
    assert((doc.apis.map(api => api.path).toSet &
      Set("/user",
        "/user/createWithArray",
        "/user/createWithList")).size == 3)

    var param = doc.apis.filter(api => api.path == "/user/createWithList")(0).operations(0).parameters(0)
    assert(param.dataType === "List[User]")
  }

  ignore should "read the pet api description in XML" in {
    val xmlString = Source.fromURL("http://localhost:8002/api/api-docs/pet").mkString
    val xml = scala.xml.XML.loadString(xmlString)

    assert(((xml \ "apis").map(api => (api \ "path").text).toSet &
      Set("/pet/{petId}",
        "/pet/findByStatus",
        "/pet/findByTags")).size == 3)
  }
}
