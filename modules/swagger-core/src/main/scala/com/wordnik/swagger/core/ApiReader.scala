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

package com.wordnik.swagger.core

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util.TypeUtil

import org.slf4j.LoggerFactory

import java.lang.reflect.{ Type, Field, Modifier, Method }
import java.lang.annotation.Annotation
import javax.xml.bind.annotation._

import scala.collection.JavaConversions._
import collection.mutable.ListBuffer

object ApiMethodType {
  val GET = "GET"
  val PUT = "PUT"
  val DELETE = "DELETE"
  val POST = "POST"
  val HEAD = "HEAD"
}

trait ApiSpecParserTrait extends BaseApiParser {
  private val LOGGER = LoggerFactory.getLogger(classOf[ApiSpecParserTrait])

  def hostClass: Class[_]
  def documentation: Documentation
  def apiEndpoint: Api
  def apiVersion: String
  def swaggerVersion: String
  def basePath: String
  def resourcePath: String
  def getPath(method: Method): String
  def processParamAnnotations(docParam: DocumentationParameter, paramAnnotations: Array[Annotation], method: Method): Boolean

  val TRAIT = "trait"

  // implement this to detect HTTP method type
  def parseHttpMethod(method: Method, apiOperation: ApiOperation): String

  def parse(): Documentation = {
    if (apiEndpoint != null)
      hostClass.getMethods.foreach(method => parseMethod(documentation, method))
    documentation.apiVersion = apiVersion
    documentation.swaggerVersion = swaggerVersion
    documentation.basePath = basePath
    documentation.resourcePath = resourcePath
    documentation
  }

  def parseApiParam(docParam: DocumentationParameter, apiParam: ApiParam, method: Method) {
    docParam.name = readString(apiParam.name, docParam.name)
    docParam.description = readString(apiParam.value)
    docParam.defaultValue = readString(apiParam.defaultValue)
    try {
      docParam.allowableValues = convertToAllowableValues(apiParam.allowableValues)
    } catch {
      case e: RuntimeException =>
        LOGGER.error("Allowable values annotation problem in method  " + method +
          "for parameter " + docParam.name)
        e.printStackTrace()
    }
    docParam.required = apiParam.required
    docParam.allowMultiple = apiParam.allowMultiple
    docParam.paramAccess = readString(apiParam.access)
  }

  // foo.bar.X<foo.Y,bar.Z> will add X,Y,Z to modules and return "X","Y,Z"
  // foo.bar.X<foo.Y> will add X,Y to document's modules and return "X","Y"
  private def handleGenerics(apiResponseValue: String) : (String, String) = {
    val idx1 = apiResponseValue.indexOf('<')
    val idx2 = apiResponseValue.indexOf('>')
    val baseClass = apiResponseValue.substring(0, idx1)
    val name = addClassToModels(documentation, baseClass)

    val genericClasses = apiResponseValue.substring(idx1 + 1, idx2)
    var genericName = new String
    if (genericClasses.contains(",")) {

      for (gc <- genericClasses.split(',')) {
        genericName += addClassToModels(documentation, gc) + ","
      }
      //remove last ','
      genericName = genericName.substring(0, genericName.length - 1)
    } else {
      genericName = addClassToModels(documentation, genericClasses)
    }
    (name, genericName)
  }

  private val ListRegex: scala.util.matching.Regex = """List\[(.*?)\]""".r
  def parseMethod(documentation: Documentation, method: Method): Any = {
    val apiOperation = method.getAnnotation(classOf[ApiOperation])
    val apiErrors = method.getAnnotation(classOf[ApiErrors])
    val isDeprecated = method.getAnnotation(classOf[Deprecated])

    LOGGER.debug("parsing method " + method.getName)
    if (apiOperation != null && method.getName != "getHelp") {
      // Read the Operation
      val docOperation = new DocumentationOperation

      // check if its deprecated
      if (isDeprecated != null) docOperation.deprecated = true

      if (apiOperation != null) {
        docOperation.httpMethod = parseHttpMethod(method, apiOperation)
        docOperation.summary = readString(apiOperation.value)
        docOperation.notes = readString(apiOperation.notes)
        docOperation.setTags(toObjectList(apiOperation.tags))
        docOperation.nickname = method.getName
        val (apiResponseValue: String, isResponseMultiValue: Boolean) = 
          ((responseClass: String, isMulti: Boolean) => responseClass match {
            case ListRegex(respClass) => (respClass, true)
            case _ => (responseClass, isMulti)
          }
        )(readString(apiOperation.responseClass), apiOperation.multiValueResponse)

        LOGGER.debug("apiOperation apiResponseValue: " + apiResponseValue)

        val p = """^([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z0-9_]+)*(<([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z0-9_]+)*(,([a-zA-Z_][a-zA-Z0-9_]*)(\.[a-zA-Z0-9_]+)*)*>)+$"""
        if (apiResponseValue.matches(p)) {
          val className = handleGenerics(apiResponseValue)
          docOperation.responseClass = {
            if (isResponseMultiValue) "List[" + className._1 + "<" + className._2 + ">]" else className._1 + "<" + className._2 +">"
          }
        } else {
          docOperation.setResponseTypeInternal(apiResponseValue)
          try {
            val name = {
              if (ApiPropertiesReader.manualModelMapping.contains(apiResponseValue)) {
                ApiPropertiesReader.manualModelMapping(apiResponseValue)._1
              }
              else {
                val cls = SwaggerContext.loadClass(apiResponseValue)
                LOGGER.debug("loaded class " + cls)
                ApiPropertiesReader.readName(cls)
              }
             }
            docOperation.responseClass = {
              if (isResponseMultiValue) "List[" + name + "]" else name
            }
          } catch {
            case e: ClassNotFoundException => docOperation.responseClass = {
              if (isResponseMultiValue) "List[" + apiResponseValue + "]" else apiResponseValue
            }
          }
        }
      }

      // Read method annotations for implicit api params which are not declared as actual argments to the method
      // Essentially ApiParamImplicit annotations on method
      val methodAnnotations = method.getAnnotations
      for (ma <- methodAnnotations) {
        ma match {
          case pSet: ApiParamsImplicit => {
            for (p <- pSet.value()) {
              val docParam = new DocumentationParameter
              docParam.paramType = TYPE_QUERY

              docParam.name = readString(p.name)
              docParam.description = readString(p.value)
              docParam.defaultValue = readString(p.defaultValue)
              try {
                docParam.allowableValues = convertToAllowableValues(p.allowableValues)
              } catch {
                case e: RuntimeException =>
                  LOGGER.error("Allowable values annotation problem in method  " + method +
                    "for parameter " + docParam.name)
                  e.printStackTrace()
              }
              docParam.required = p.required
              docParam.allowMultiple = p.allowMultiple
              docParam.paramAccess = readString(p.access)
              docParam.internalDescription = readString(p.internalDescription)
              docParam.paramType = readString(p.paramType)
              docParam.paramType = if (docParam.paramType == null) TYPE_QUERY else docParam.paramType

              val dataType = readString(p.dataType)
              docParam.setValueTypeInternal(dataType)
              try {
                val cls = SwaggerContext.loadClass(dataType)
                docParam.dataType = ApiPropertiesReader.readName(cls)
              } catch {
                case e: ClassNotFoundException => docParam.dataType = dataType
              }
              docOperation.addParameter(docParam)
            }
          }
          case _ => Unit
        }
      }

      // Read the params and add to Operation
      val paramAnnotationDoubleArray = method.getParameterAnnotations
      val paramTypes = method.getParameterTypes
      val genericParamTypes = method.getGenericParameterTypes
      var counter = 0
      var ignoreParam = false
      paramAnnotationDoubleArray.foreach(paramAnnotations => {
        val docParam = new DocumentationParameter
        docParam.required = true

        // determine value type
        try {
          val paramTypeClass = paramTypes(counter)
          val paramTypeName = ApiPropertiesReader.getDataType(genericParamTypes(counter), paramTypeClass);
          docParam.dataType = {
            if (ApiPropertiesReader.manualModelMapping.contains(paramTypeName)) {
              ApiPropertiesReader.manualModelMapping(paramTypeName)._1
            }
            else
              paramTypeName
          }
          if (!paramTypeClass.isPrimitive && !paramTypeClass.getName().contains("java.lang")) {
            docParam.setValueTypeInternal(ApiPropertiesReader.getGenericTypeParam(genericParamTypes(counter), paramTypeClass))
          }
        } catch {
          case e: Exception => LOGGER.debug("Unable to determine datatype for param " + counter + " in method " + method, e)
        }

        ignoreParam = processParamAnnotations(docParam, paramAnnotations, method)

        if (paramAnnotations.length == 0) {
          ignoreParam = true
        }

        counter = counter + 1

        // Set the default paramType, if nothing is assigned
        docParam.paramType = readString(TYPE_BODY, docParam.paramType)
        if (!ignoreParam) docOperation.addParameter(docParam)
      })

      // Get Endpoint
      val docEndpoint = getEndPoint(documentation, getPath(method))

      // Add Operation to Endpoint
      docEndpoint.addOperation(processOperation(method, docOperation))
      LOGGER.debug("added operation " + docOperation + " from method " + method.getName)

      // Read the Errors and add to Response
      if (apiErrors != null) {
        for (apiError <- apiErrors.value) {
          val docError = new DocumentationError
          docError.code = apiError.code
          docError.reason = readString(apiError.reason)
          docOperation.addErrorResponse(docError)
        }
      }
    } else LOGGER.debug("skipping method " + method.getName)
  }

  private def addClassToModels(d: Documentation, t: String) : String = {
    try {
      val n = ApiPropertiesReader.read(t)
      if (null != n && null != n.getFields && n.getFields.length > 0) {
        d.addModel(n.getName, n.toDocumentationSchema())
      } else {
        if (null == n) LOGGER.error("Skipping model " + t + ". Could not load the model.")
        else if (null == n.getFields || n.getFields.length == 0)
          LOGGER.error("Skipping model " + t + ". Did not find any public fields or bean-properties in this model. If its a scala class its fields might not have @BeanProperty annotation added to its fields.")
      }
      n.getName
    } catch {
      case e: ClassNotFoundException => LOGGER.error("Unable to resolve class " + t); t
      case e: Exception => LOGGER.error("Unable to load model documentation for " + t, e); t
    }
  }

  protected def processOperation(method: Method, o: DocumentationOperation) = o

  private def getEndPoint(documentation: Documentation, path: String): DocumentationEndPoint = {
    var ep: DocumentationEndPoint = null

    if (documentation.getApis != null)
      documentation.getApis.foreach(endpoint => if (endpoint.path == path) ep = endpoint)

    if (ep == null) {
      val o = new DocumentationEndPoint(path, apiEndpoint.description)
      documentation.addApi(o)
      o
    } else ep
  }

  private def getCategory(method: Method): String = {
    val declaringInterface = ReflectionUtil.getDeclaringInterface(method)
    if (declaringInterface == null)
      null
    else {
      val simpleName = declaringInterface.getSimpleName
      if (simpleName.toLowerCase.endsWith(TRAIT) && simpleName.length > TRAIT.length)
        simpleName.substring(0, simpleName.length - TRAIT.length)
      else
        simpleName
    }
  }
}
