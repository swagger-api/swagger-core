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

import com.wordnik.util.perf._

import org.junit.runner.RunWith

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.JavaConversions._

import scala.io._

@RunWith(classOf[JUnitRunner])
class ResourceListingIT extends FlatSpec with ShouldMatchers {
  it should "read a resource listing" in {
    val json = Source.fromURL("http://localhost:8002/api/api-docs.json").mkString
    val doc = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    assert(doc.getApis.size === 3)
    assert((doc.getApis.map(api => api.getPath).toSet &
      Set("/api-docs.{format}/pet",
        "/api-docs.{format}/user",
        "/api-docs.{format}/health")).size == 3)
  }

  it should "read the pet api description" in {
    val json = Source.fromURL("http://localhost:8002/api/api-docs.json/health").mkString
    val doc = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    assert(doc.getApis.size === 1)
    assert((doc.getApis.map(api => api.getPath).toSet &
      Set("/health.{format}/profile")).size == 1)
  }

  it should "increment a profile counter" in {
    // register 10 counts
    (1 to 10).foreach(i => Source.fromURL("http://localhost:8002/api/pet.json/2").mkString)

    val json = Source.fromURL("http://localhost:8002/api/health.json/profile").mkString
    val counters = JsonUtil.getJsonMapper.readValue(json, classOf[Array[ProfileCounter]])

    assert(counters.size == 1)
    assert(counters(0).key == "/pet/*")
    assert(counters(0).count == 10)
  }
}
