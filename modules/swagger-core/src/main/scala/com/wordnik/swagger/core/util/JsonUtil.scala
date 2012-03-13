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

package com.wordnik.swagger.core.util

import org.codehaus.jackson.map._
import org.codehaus.jackson.map.DeserializationConfig.Feature
import org.codehaus.jackson.map.annotate.JsonSerialize
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector

object JsonUtil {
  def getJsonMapper = {
    val jackson = new JacksonAnnotationIntrospector()
    val jaxb = new JaxbAnnotationIntrospector()
    val pair = new AnnotationIntrospector.Pair(jaxb, jackson)
    val mapper = new ObjectMapper()

    mapper.getSerializationConfig().setAnnotationIntrospector(jaxb);
    mapper.getDeserializationConfig().setAnnotationIntrospector(pair);
    mapper.getDeserializationConfig().set(Feature.AUTO_DETECT_SETTERS, true);
    mapper.configure(Feature.AUTO_DETECT_SETTERS, true);
    mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
    mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper
  }
}