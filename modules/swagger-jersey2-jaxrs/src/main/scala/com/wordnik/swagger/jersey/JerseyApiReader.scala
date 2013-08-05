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

package com.wordnik.swagger.jersey

import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.core._
import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util.TypeUtil

import org.slf4j.LoggerFactory

import javax.ws.rs._
import core.Context

import java.lang.reflect.{ Type, Field, Modifier, Method }
import java.lang.annotation.Annotation

import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam

class JerseyApiReader extends JaxrsApiReader {
  private val LOGGER = LoggerFactory.getLogger(classOf[JerseyApiReader])

  def processParamAnnotations(mutable: MutableParameter, paramAnnotations: Array[Annotation]): Option[Parameter] = {
    var shouldIgnore = false
    for (pa <- paramAnnotations) {
      pa match {
        case e: ApiParam => parseApiParamAnnotation(mutable, e)
        case e: QueryParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_QUERY, mutable.paramType)
        }
        case e: PathParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.required = true
          mutable.paramType = readString(TYPE_PATH, mutable.paramType)
        }
        case e: MatrixParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_MATRIX, mutable.paramType)
        }
        case e: HeaderParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_HEADER, mutable.paramType)
        }
        case e: FormParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_FORM, mutable.paramType)
        }
        case e: CookieParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_COOKIE, mutable.paramType)
        }
        case e: FormDataParam => {
          mutable.dataType match {
            case "java.io.InputStream" => {
              mutable.name = readString(e.value, mutable.name)
              mutable.paramType = "body"
              mutable.dataType = "File"
            }
            case "file" => 
            case "org.glassfish.jersey.media.multipart.FormDataContentDisposition" => shouldIgnore = true
            case _ => {
              mutable.name = readString(e.value, mutable.name)
              mutable.paramType = readString(TYPE_FORM, mutable.paramType)
            }
          }
        }
        case e: Context => shouldIgnore = true
        case _ =>
      }
    }
    if(!shouldIgnore) {
      if(mutable.paramType == null) {
        mutable.paramType = TYPE_BODY
        mutable.name = TYPE_BODY
      }
      Some(mutable.asParameter)
    }
    else None
  }
}

