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

package com.wordnik.test.swagger.core

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiPropertiesReader

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class TypeUtilTest extends FlatSpec with ShouldMatchers {
  it should "extract required classes" in {
    val refs = TypeUtil.getReferencedClasses("com.wordnik.test.swagger.core.House").asScala.toSet

    ApiPropertiesReader.excludedFieldTypes ++= Seq("Formats", "JsonLike", "Json4S")

    val docObj = ApiPropertiesReader.read("com.wordnik.test.swagger.core.House")
    (Set(
      "com.wordnik.test.swagger.core.House", 
      "com.wordnik.test.swagger.core.Furniture", 
      "com.wordnik.test.swagger.core.Window") & refs
    ).size should be (3)
  }
}

import org.json4s._
import jackson.{Serialization, JsonMethods}
import org.json4s.jackson.Serialization.write
 

case class Window(description: String)
case class Furniture(description: String)
case class House(name: String,
  windows: Option[Seq[Window]],
  furniture: Option[Seq[Furniture]]) extends Json4SModule
 
trait Json4SModule extends JsonModule {
  type JsonBackend = Json4S
 
  implicit val formats = (DefaultFormats + NoTypeHints) ++ org.json4s.ext.JodaTimeSerializers.all
 
  class Json4S extends JsonLike {
    this: JsonBackend =>
 
    def generate[A <: AnyRef : Manifest](obj: A): String = write[A](obj)
 
    def parse[A](input: String)(implicit m : Manifest[A]): A = JsonMethods.parse(input).extract[A]
 
    def toMap(input: String): Map[String, Any] = JsonMethods.parse(input).asInstanceOf[JObject].values
  }
  override val Json = new Json4S
}
 
trait JsonModule {
 
  type JsonBackend <: JsonLike
 
  trait JsonLike {
    this: JsonBackend =>
 
    def generate[A <: AnyRef : Manifest](obj: A): String
 
    def parse[A](input: String)(implicit m: Manifest[A]): A
  }
 
  def Json : JsonBackend
}
