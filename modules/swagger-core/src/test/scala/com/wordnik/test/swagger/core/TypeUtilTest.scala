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

import com.wordnik.swagger.core.util._

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class TypeUtilTest extends FlatSpec with ShouldMatchers {
  it should "extract required classes" in {
    val refs = TypeUtil.getReferencedClasses("com.wordnik.test.swagger.core.House").asScala.toSet
    (Set(
      "com.wordnik.test.swagger.core.House", 
      "com.wordnik.test.swagger.core.Furniture", 
      "com.wordnik.test.swagger.core.Window") & refs
    ).size should be (3)
  }
}

case class Window(description: String)
case class Furniture(description: String)
case class House(
  windows: Option[Seq[Window]] = None,
  furniture: Option[Seq[Furniture]] = None)
