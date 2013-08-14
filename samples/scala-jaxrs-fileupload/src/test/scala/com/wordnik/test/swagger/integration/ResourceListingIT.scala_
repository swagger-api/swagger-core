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
    assert(doc.getApis.size === 2)
    assert((doc.getApis.map(api => api.getPath).toSet &
      Set("/api-docs.{format}/pet",
        "/api-docs.{format}/user")).size == 2)
  }

  it should "read the pet api description" in {
    val json = Source.fromURL("http://localhost:8002/api/api-docs.json/pet").mkString
    val doc = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    assert(doc.getApis.size === 4)
    assert((doc.getApis.map(api => api.getPath).toSet &
      Set("/pet.{format}/{petId}",
        "/pet.{format}/findByStatus",
        "/pet.{format}/uploadImage",
        "/pet.{format}/findByTags")).size == 4)
  }

  it should "have form params" in {
    val json = Source.fromURL("http://localhost:8002/api/api-docs.json/pet").mkString
    val doc = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    assert(doc.getApis.size === 4)
    val ep = doc.getApis.filter(m => m.getPath == "/pet.{format}/uploadImage").head
    ep.getOperations.size should be (1)

    println(JsonUtil.getJsonMapper.writeValueAsString(ep))

    val op = ep.getOperations.head

    op.httpMethod should be ("POST")
    op.getParameters.size should be (2)

    val metadata = op.getParameters.filter(m => m.name == "additionalMetadata").head
    metadata.dataType should be ("string")
    metadata.paramType should be ("form")

    val fileUpload = op.getParameters.filter(m => m.name == "file").head
    fileUpload.dataType should be ("file")
    fileUpload.paramType should be ("body")

  }

  it should "read the user api with array and list data types as post data" in {
    val json = Source.fromURL("http://localhost:8002/api/api-docs.json/user").mkString
    val doc = JsonUtil.getJsonMapper.readValue(json, classOf[Documentation])
    assert(doc.getApis.size === 6)
    assert((doc.getApis.map(api => api.getPath).toSet &
      Set("/user.{format}",
        "/user.{format}/createWithArray",
        "/user.{format}/createWithList")).size == 3)

    var param = doc.getApis.filter(api => api.getPath == "/user.{format}/createWithList")(0).getOperations()(0).getParameters()(0)
    assert(param.getDataType() === "List[User]")
  }
}