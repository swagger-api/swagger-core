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
    val json = Source.fromURL("http://localhost:8002/resteasy/api-docs").mkString
    val doc = ScalaJsonUtil.mapper.readValue(json, classOf[ResourceListing])
    doc.apis.size should be (1)
    (doc.apis.map(api => api.path).toSet & Set("/library")).size should be (1)
  }

  it should "read an api declaration" in {
    val json = Source.fromURL("http://localhost:8002/resteasy/api-docs/library").mkString
    val doc = ScalaJsonUtil.mapper.readValue(json, classOf[ApiListing])
    doc.apis.size should be (2)
    (doc.apis.map(api => api.path).toSet & Set("/library/books/badger", "/library/books/mapped")).size should be (2)
    (doc.models.get.map(_._1).toSet & Set("listing", "book")).size should be (2)
  }
}