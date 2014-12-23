/**
 *  Copyright 2014 Reverb Technologies, Inc.
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

package com.wordnik.swagger.core.util

import com.wordnik.swagger.model._

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import Xml._
import scala.xml.{Group, Text}

object JsonSerializer {
  implicit val formats = SwaggerSerializers.formats
  val printer = new scala.xml.PrettyPrinter(100,2)

  def asJson(w: AnyRef): String = {
  	write(w)
  }

  def asXml(w: AnyRef): String = {
  	toXml(parse(write(w))).toString
  }

  def asResourceListing(json: String): ResourceListing = {
    parse(json).extract[ResourceListing]
  }

  def asApiListing(json: String): ApiListing = {
  	parse(json).extract[ApiListing]
  }
}