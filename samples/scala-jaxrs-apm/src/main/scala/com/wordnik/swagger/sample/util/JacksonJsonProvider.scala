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

package com.wordnik.swagger.sample.resource

import javax.ws.rs.Produces

import javax.ws.rs.core.MediaType
import javax.ws.rs.ext.Provider

import com.wordnik.swagger.core.util.JsonUtil

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.databind._

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import com.fasterxml.jackson.core.JsonGenerator.Feature
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.annotation._
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
class JacksonJsonProvider extends JacksonJaxbJsonProvider {
  val commonMapper = new ObjectMapper()
  commonMapper.registerModule(new DefaultScalaModule())
  commonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  commonMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
  commonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  super.setMapper(commonMapper)
}