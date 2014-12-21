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
import com.wordnik.swagger.config._
import com.wordnik.swagger.annotations._

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util.TypeUtil

import org.slf4j.LoggerFactory

import javax.ws.rs._
import core.Context

import java.lang.reflect.{ ParameterizedType, Type, Field, Modifier, Method }
import java.lang.annotation.Annotation

import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam

import scala.collection.mutable.{ ListBuffer, HashMap, HashSet }

class JerseyApiReader extends JaxrsApiReader {
  private val LOGGER = LoggerFactory.getLogger(classOf[JerseyApiReader])

  def readRecursive(
    docRoot: String, 
    parentPath: String, cls: Class[_], 
    config: SwaggerConfig,
    operations: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]],
    parentMethods: ListBuffer[Method]): Option[ApiListing] = {
    val api = cls.getAnnotation(classOf[Api])

    // must have @Api annotation to process!
    if(api != null) {
      val consumes = Option(api.consumes) match {
        case Some(e) if(e != "") => e.split(",").map(_.trim).toList
        case _ => cls.getAnnotation(classOf[Consumes]) match {
          case e: Consumes => e.value.toList
          case _ => List()
        }
      }
      val produces = Option(api.produces) match {
        case Some(e) if(e != "") => e.split(",").map(_.trim).toList
        case _ => cls.getAnnotation(classOf[Produces]) match {
          case e: Produces => e.value.toList
          case _ => List()
        }
      }
      val protocols = Option(api.protocols) match {
        case Some(e) if(e != "") => e.split(",").map(_.trim).toList
        case _ => List()
      }
      val description = api.description match {
        case e: String if(e != "") => Some(e)
        case _ => None
      }
      // look for method-level annotated properties
      val parentParams: List[Parameter] = getAllParamsFromFields(cls)

      for(method <- cls.getMethods) {
        val returnType = findSubresourceType(method)
        val path = method.getAnnotation(classOf[Path]) match {
          case e: Path => e.value()
          case _ => ""
        }
        val endpoint = (parentPath + pathFromMethod(method)).replace("//", "/")
        Option(returnType.getAnnotation(classOf[Api])) match {
          case Some(e) => {
            val root = docRoot + api.value + pathFromMethod(method)
            parentMethods += method
            readRecursive(root, endpoint, returnType, config, operations, parentMethods)
            parentMethods -= method
          }
          case _ => {
            if(method.getAnnotation(classOf[ApiOperation]) != null) {
              readMethod(method, parentParams, parentMethods) match {
                case Some(op) => Some(appendOperation(endpoint, path, op, operations))
                case None => None
              }
            }
          }
        }
      }
      // sort them by min position in the operations
      val s = (for(op <- operations) yield {
        (op, op._3.map(_.position).toList.min)
      }).sortWith(_._2 < _._2).toList
      val orderedOperations = new ListBuffer[Tuple3[String, String, ListBuffer[Operation]]]
      s.foreach(op => {
        val ops = op._1._3.sortWith(_.position < _.position)
        orderedOperations += Tuple3(op._1._1, op._1._2, ops)
      })
      val apis = (for ((endpoint, resourcePath, operationList) <- orderedOperations) yield {
        val orderedOperations = new ListBuffer[Operation]
        operationList.sortWith(_.position < _.position).foreach(e => orderedOperations += e)
        ApiDescription(
          addLeadingSlash(endpoint),
          None,
          orderedOperations.toList,
          api.hidden)
      }).toList
      val models = ModelUtil.modelsFromApis(apis)
      val basePath = {
        if(api.basePath == "")
          config.basePath
        else
          api.basePath
      }
      Some(ApiListing (
        apiVersion = config.apiVersion,
        swaggerVersion = config.swaggerVersion,
        basePath = basePath,
        resourcePath = addLeadingSlash(api.value),
        apis = ModelUtil.stripPackages(apis),
        models = models,
        description = description,
        produces = produces,
        consumes = consumes,
        protocols = protocols,
        position = api.position)
      )
    }
    else None
  }

  def processParamAnnotations(mutable: MutableParameter, paramAnnotations: Array[Annotation]): List[Parameter] = {
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
        case e: BeanParam => {
          val cls = SwaggerContext.loadClass(mutable.dataType)
          return getAllParamsFromFields(cls)
        }
        case e: DefaultValue => {
          mutable.defaultValue = Option(readString(e.value))
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
      List(mutable.asParameter)
    }
    else List.empty
  }
  def findSubresourceType(method: Method): Class[_] = {
    method.getGenericReturnType match {
      case c: Class[_] => c
      case pt: ParameterizedType =>
        val typeArguments = pt.getActualTypeArguments
        if (pt.getRawType.equals(classOf[Class[_]]) && typeArguments.length == 1)
          typeArguments(0) match {
            case c: Class[_] => c
            case _ => method.getReturnType
          }
        else
          method.getReturnType
      case _ => method.getReturnType
    }
  }
}

