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

package com.wordnik.test.swagger.integration

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util.JsonUtil

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.config.DefaultClientConfig

import org.junit.runner.RunWith

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.JavaConversions._

import scala.io._

@RunWith(classOf[JUnitRunner])
class ResourceListingIT extends FlatSpec with ShouldMatchers {
  it should "read a resource listing in JSON" in {
    val json = Client.create(new DefaultClientConfig()).resource("http://localhost:8002/api/resources").accept("application/json").get(classOf[String])
    val doc = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    assert(doc.getApis.size === 2)
    assert((doc.getApis.map(api => api.getPath).toSet & Set("/resources/pet", "/resources/user")).size == 2)
  }

  it should "read a resource listing in XML" in {
    val xmlString = Client.create(new DefaultClientConfig()).resource("http://localhost:8002/api/resources").accept("application/xml").get(classOf[String])
    val xml = scala.xml.XML.loadString(xmlString)
    assert(((xml \ "apis").map(api => (api \ "path").text).toSet & Set("/resources/pet", "/resources/user")).size == 2)
  }

  it should "read the pet api description in JSON" in {
    val json = Client.create(new DefaultClientConfig()).resource("http://localhost:8002/api/resources/pet").accept("application/json").get(classOf[String])
    val doc = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    assert(doc.getApis.size === 3)
    assert((doc.getApis.map(api => api.getPath).toSet &
      Set("/pet/{petId}",
        "/pet/findByStatus",
        "/pet/findByTags")).size == 3)
  }
}
