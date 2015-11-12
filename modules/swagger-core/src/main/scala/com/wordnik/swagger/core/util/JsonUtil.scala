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

import com.fasterxml.jackson.annotation.JsonInclude._
import com.fasterxml.jackson.core.JsonGenerator.Feature
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object JsonUtil {
  val m = new ObjectMapper()
  m.setSerializationInclusion(Include.NON_NULL);
  m.setSerializationInclusion(Include.NON_DEFAULT)
  m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  m.setSerializationInclusion(Include.NON_EMPTY)

  def mapper = m
}

object ScalaJsonUtil {
  val m = new ObjectMapper()
  m.setSerializationInclusion(Include.NON_EMPTY)
  m.registerModule(new DefaultScalaModule())
  m.setSerializationInclusion(Include.NON_NULL);
  m.setSerializationInclusion(Include.NON_DEFAULT)
  m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def mapper = m
}