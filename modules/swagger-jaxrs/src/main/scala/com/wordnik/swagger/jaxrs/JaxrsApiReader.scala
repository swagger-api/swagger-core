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

package com.wordnik.swagger.jaxrs

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util.TypeUtil

import org.slf4j.LoggerFactory

import javax.ws.rs._
import core.Context
import util.ReflectionUtil

import java.lang.reflect.{ Type, Field, Modifier, Method }
import java.lang.annotation.Annotation
import javax.xml.bind.annotation._

import scala.collection.JavaConversions._
import collection.mutable.ListBuffer

object JaxrsApiReader {
  private val LOGGER = LoggerFactory.getLogger(JaxrsApiReader.getClass)

  var FORMAT_STRING = ".{format}"
  val LIST_RESOURCES_PATH = "/resources"

  private val endpointsCache = scala.collection.mutable.Map.empty[Class[_], Documentation]

  def setFormatString(str: String) = {
    LOGGER.debug("setting format string")
    if (FORMAT_STRING != str) {
      LOGGER.debug("clearing endpoint cache")
      endpointsCache.clear
      FORMAT_STRING = str
    }
  }

  def read(hostClass: Class[_], apiVersion: String, swaggerVersion: String, basePath: String, apiPath: String): Documentation = {
    LOGGER.debug("reading path " + apiPath)

    endpointsCache.get(hostClass) match {
      case None => {
        val doc = new JaxrsApiSpecParser(hostClass, apiVersion, swaggerVersion, basePath, apiPath).parse
        endpointsCache += hostClass -> doc.clone.asInstanceOf[Documentation]
        doc
      }
      case doc: Option[Documentation] => doc.get.clone.asInstanceOf[Documentation]
      case _ => null
    }
  }
}

class JaxrsApiSpecParser(val _hostClass: Class[_], _apiVersion: String, _swaggerVersion: String, _basePath: String, _resourcePath: String)
  extends ApiSpecParserTrait {
  private val LOGGER = LoggerFactory.getLogger(classOf[JaxrsApiSpecParser])

  override def hostClass = _hostClass
  override def apiVersion = _apiVersion
  override def swaggerVersion = _swaggerVersion
  override def basePath = _basePath
  override def resourcePath = _resourcePath

  LOGGER.debug(hostClass + ", apiVersion: " + apiVersion + ", swaggerVersion: " + swaggerVersion + ", basePath: " + basePath + ", resourcePath: " + resourcePath)

  val documentation = new Documentation
  val apiEndpoint = hostClass.getAnnotation(classOf[Api])

  override def processParamAnnotations(docParam: DocumentationParameter, paramAnnotations: Array[Annotation], method: Method): Boolean = {
    var ignoreParam = false
    for (pa <- paramAnnotations) {
      pa match {
        case apiParam: ApiParam => parseApiParam(docParam, apiParam, method)
        case wsParam: QueryParam => {
          docParam.name = readString(wsParam.value, docParam.name)
          docParam.paramType = readString(TYPE_QUERY, docParam.paramType)
        }
        case wsParam: PathParam => {
          docParam.name = readString(wsParam.value, docParam.name)
          docParam.required = true
          docParam.paramType = readString(TYPE_PATH, docParam.paramType)
        }
        case wsParam: MatrixParam => {
          docParam.name = readString(wsParam.value, docParam.name)
          docParam.paramType = readString(TYPE_MATRIX, docParam.paramType)
        }
        case wsParam: HeaderParam => {
          docParam.name = readString(wsParam.value, docParam.name)
          docParam.paramType = readString(TYPE_HEADER, docParam.paramType)
        }
        case wsParam: FormParam => {
          docParam.name = readString(wsParam.value, docParam.name)
          docParam.paramType = readString(TYPE_FORM, docParam.paramType)
        }
        case wsParam: CookieParam => {
          docParam.name = readString(wsParam.value, docParam.name)
          docParam.paramType = readString(TYPE_COOKIE, docParam.paramType)
        }
        case wsParam: Context => ignoreParam = true
        case _ => Unit
      }
    }
    ignoreParam
  }

  override def getPath(method: Method): String = {
    val wsPath = method.getAnnotation(classOf[javax.ws.rs.Path])
    val path = apiEndpoint.value + JaxrsApiReader.FORMAT_STRING + (if (wsPath == null) "" else wsPath.value)
    path
  }

  override def parseHttpMethod(method: Method, apiOperation: ApiOperation): String = {
    if (apiOperation.httpMethod() != null && apiOperation.httpMethod().trim().length() > 0)
      apiOperation.httpMethod().trim()
    else {
      val wsGet = method.getAnnotation(classOf[javax.ws.rs.GET])
      val wsDelete = method.getAnnotation(classOf[javax.ws.rs.DELETE])
      val wsPost = method.getAnnotation(classOf[javax.ws.rs.POST])
      val wsPut = method.getAnnotation(classOf[javax.ws.rs.PUT])
      val wsHead = method.getAnnotation(classOf[javax.ws.rs.HEAD])

      if (wsGet != null) ApiMethodType.GET
      else if (wsDelete != null) ApiMethodType.DELETE
      else if (wsPost != null) ApiMethodType.POST
      else if (wsPut != null) ApiMethodType.PUT
      else if (wsHead != null) ApiMethodType.HEAD
      else null
    }
  }
}