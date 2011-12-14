/**
 *  Copyright 2011 Wordnik, Inc.
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

import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util.TypeUtil

import org.slf4j.LoggerFactory

import javax.ws.rs._
import core.Context
import util.ReflectionUtil

import java.lang.reflect.{Type, Field, Modifier, Method}
import java.lang.annotation.Annotation
import javax.xml.bind.annotation._

import scala.collection.JavaConversions._
import collection.mutable.ListBuffer

object ApiReader {
  val GET = "GET";
  val PUT = "PUT";
  val DELETE = "DELETE";
  val POST = "POST";
  val HEAD = "HEAD";


  val FORMAT_STRING = "{format}";
  val LIST_RESOURCES_PATH = "/resources";

  val POSITIVE_INFINITY_STRING = "Infinity"
  val NEGATIVE_INFINITY_STRING = "-Infinity"


  private val endpointsCache = scala.collection.mutable.Map.empty[Class[_], Documentation]

  def read(hostClass: Class[_], apiVersion: String, swaggerVersion: String, basePath: String, apiPath:String ): Documentation = {
    endpointsCache.get(hostClass) match {
      case None => val doc = new ApiSpecParser(hostClass, apiVersion, swaggerVersion, basePath, apiPath).parse; endpointsCache += hostClass -> doc.clone.asInstanceOf[Documentation]; doc
      case doc: Option[Documentation] => doc.get.clone.asInstanceOf[Documentation]
      case _ => null
    }
  }
}

trait BaseApiParser {
  protected def readString(s: String, existingValue: String = null, ignoreValue: String = null): String = {
    if (existingValue != null && existingValue.trim.length > 0)
      existingValue
    else if (s == null)
      null
    else if (s.trim.length == 0)
      null
    else if (ignoreValue != null && s.equals(ignoreValue))
      null
    else
      s.trim
  }

  protected def toObjectList(csvString: String, paramType: String = null) = {
    if(csvString == null || csvString.length == 0) new ListBuffer[String].toList
    else{
      val params = csvString.split(",").toList
      paramType match {
        case null => params
        case "string" => params

      }
    }
  }

  protected def convertToAllowableValues(csvString: String, paramType: String = null):DocumentationAllowableValues = {
    if (csvString.toLowerCase.startsWith("range[")){
      val ranges = csvString.substring(6, csvString.length()-1).split(",")
      return buildAllowableRangeValues(ranges,csvString)
    }else if (csvString.toLowerCase.startsWith("rangeexclusive[")){
      val ranges = csvString.substring(15, csvString.length()-1).split(",")
      return buildAllowableRangeValues(ranges,csvString)
    }else{
      if(csvString == null || csvString.length == 0){
        null
      }
      else{
        val params = csvString.split(",").toList
        paramType match {
          case null => new DocumentationAllowableListValues(params)
          case "string" => new DocumentationAllowableListValues(params)
        }
      }
    }
  }

  private def buildAllowableRangeValues(ranges:Array[String], inputStr:String):DocumentationAllowableRangeValues = {
    var min:java.lang.Float = 0
    var max:java.lang.Float = 0
    if (ranges.size < 2){
      throw new RuntimeException("Allowablevalues format " + inputStr + "is not right");
    }
    if (ranges(0).equalsIgnoreCase(ApiReader.POSITIVE_INFINITY_STRING)){
      min = Float.PositiveInfinity
    }
    else if (ranges(0).equalsIgnoreCase(ApiReader.NEGATIVE_INFINITY_STRING)){
      min = Float.NegativeInfinity
    }else{
      min = ranges(0).toFloat
    }
    if (ranges(1).equalsIgnoreCase(ApiReader.POSITIVE_INFINITY_STRING)){
      max = Float.PositiveInfinity
    }
    else if (ranges(1).equalsIgnoreCase(ApiReader.NEGATIVE_INFINITY_STRING)){
      max = Float.NegativeInfinity
    }else{
      max = ranges(1).toFloat
    }
    val allowableValues = new DocumentationAllowableRangeValues(min, max)
    allowableValues

  }

}

class ApiSpecParser(val hostClass: Class[_], val apiVersion: String, val swaggerVersion: String, val basePath: String, val resourcePath:String) extends BaseApiParser {
  private val LOGGER = LoggerFactory.getLogger("com.wordnik.swagger.core.ApiReader")
  private val TRAIT = "trait"

  val documentation = new Documentation

  val apiEndpoint = hostClass.getAnnotation(classOf[Api])

  def parse(): Documentation = {
    apiEndpoint match {
      case null => null
      case _ => for (method <- hostClass.getMethods) parseMethod(method)
    }
    documentation.apiVersion = apiVersion
    documentation.swaggerVersion = swaggerVersion
    documentation.basePath = basePath
    documentation.resourcePath = resourcePath
    documentation
  }

  private def parseHttpMethod(method: Method): String = {
    val wsGet = method.getAnnotation(classOf[javax.ws.rs.GET])
    val wsDelete = method.getAnnotation(classOf[javax.ws.rs.DELETE])
    val wsPost = method.getAnnotation(classOf[javax.ws.rs.POST])
    val wsPut = method.getAnnotation(classOf[javax.ws.rs.PUT])
    val wsHead = method.getAnnotation(classOf[javax.ws.rs.HEAD])

    if (wsGet != null)
      ApiReader.GET
    else if (wsDelete != null)
      ApiReader.DELETE
    else if (wsPost != null)
      ApiReader.POST
    else if (wsPut != null)
      ApiReader.PUT
    else if (wsHead != null)
      ApiReader.HEAD
    else
      null
  }

  private def parseMethod(method: Method): Any = {
    val apiOperation = method.getAnnotation(classOf[ApiOperation])
    val apiErrors = method.getAnnotation(classOf[ApiErrors])
    val isDeprecated = method.getAnnotation(classOf[Deprecated])

    if (apiOperation != null && method.getName != "getHelp") {

      // Read the Operation
      val docOperation = new DocumentationOperation

      // check if its deprecated
      if(isDeprecated != null){
        docOperation.deprecated = true
      }

      if (apiOperation != null) {
        docOperation.httpMethod = parseHttpMethod(method)
        docOperation.summary = readString(apiOperation.value)
        docOperation.notes = readString(apiOperation.notes)
        docOperation.setTags( toObjectList(apiOperation.tags) )
        docOperation.nickname = method.getName
        val apiResponseValue = readString(apiOperation.responseClass)
        val isResponseMultiValue = apiOperation.multiValueResponse

        docOperation.setResponseTypeInternal(apiResponseValue)
        try {
          val clazz = SwaggerContext.loadClass(apiResponseValue)

          val annotatedName = ApiPropertiesReader.readName(clazz)
          docOperation.responseClass = if (isResponseMultiValue) "List[" + annotatedName + "]" else annotatedName
        }
        catch {
          case e: ClassNotFoundException => docOperation.responseClass = apiResponseValue
        }
      }

      // Read the params and add to Operation
      val paramAnnotationDoubleArray = method.getParameterAnnotations
      val paramTypes = method.getParameterTypes
      var counter = 0;
      var ignoreParam = false;
      for (paramAnnotations <- paramAnnotationDoubleArray) {
        val docParam = new DocumentationParameter
        docParam.required = true

        // determine value type
        try {
          val paramTypeClass = paramTypes(counter)
          val paramTypeName = ApiPropertiesReader.readName(paramTypeClass)
          docParam.dataType = paramTypeName
          if(!paramTypeClass.isPrimitive && !paramTypeClass.getName().contains("java.lang")){
            docParam.setValueTypeInternal(paramTypeClass.getName)
          }
        }
        catch {
          case e: Exception => LOGGER.error("Unable to determine datatype for param " + counter + " in method " + method, e)
        }

        for (pa <- paramAnnotations) {
          ignoreParam = false
          pa match {
            case apiParam: ApiParam => {
              docParam.name = readString(apiParam.name, docParam.name)
              docParam.description = readString(apiParam.value)
              docParam.defaultValue = readString(apiParam.defaultValue)
              try{
                  docParam.allowableValues = convertToAllowableValues(apiParam.allowableValues)
              }catch {
                case e: RuntimeException => LOGGER.error("Allowable values annotation is wrong in method  " + method +
                  "for parameter " + docParam.name); e.printStackTrace();
              }
              docParam.required = apiParam.required
              docParam.allowMultiple = apiParam.allowMultiple
              docParam.paramAccess = readString(apiParam.access)
            };

            case wsParam: QueryParam => {
              docParam.name = readString(wsParam.value, docParam.name)
              docParam.paramType = readString(TYPE_QUERY, docParam.paramType)
            };

            case wsParam: PathParam => {
              docParam.name = readString(wsParam.value, docParam.name)
              docParam.required = true
              docParam.paramType = readString(TYPE_PATH, docParam.paramType)
            };

            case wsParam: MatrixParam => {
              docParam.name = readString(wsParam.value, docParam.name)
              docParam.paramType = readString(TYPE_MATRIX, docParam.paramType)
            };

            case wsParam: HeaderParam => {
              docParam.name = readString(wsParam.value, docParam.name)
              docParam.paramType = readString(TYPE_HEADER, docParam.paramType)
            };

            case wsParam: FormParam => {
              docParam.name = readString(wsParam.value, docParam.name)
              docParam.paramType = readString(TYPE_FORM, docParam.paramType)
            };

            case wsParam: CookieParam => {
              docParam.name = readString(wsParam.value, docParam.name)
              docParam.paramType = readString(TYPE_COOKIE, docParam.paramType)
            };

            case wsParam: Context => {
              ignoreParam = true;
            };

            case _ => Unit
          }

        }

        counter = counter + 1;

        // Set the default paramType, if nothing is assigned
        docParam.paramType = readString(TYPE_BODY, docParam.paramType)
        if(!ignoreParam) docOperation.addParameter(docParam)
      }

      // Get Endpoint
      val docEndpoint = getEndPoint(documentation, getPath(method))

      // Add Operation to Endpoint
      docEndpoint.addOperation(processOperation(method, docOperation))

      // Read the Errors and add to Response
      if (apiErrors != null) {
        for (apiError <- apiErrors.value) {
          val docError = new DocumentationError
          docError.code = apiError.code
          docError.reason = readString(apiError.reason)
          docOperation.addErrorResponse(docError)
        }
      }
    }

  }

  protected def processOperation(method: Method, o: DocumentationOperation) = o

  protected def getPath(method: Method): String = {
    val wsPath = method.getAnnotation(classOf[javax.ws.rs.Path])
    val path = apiEndpoint.value + "." + ApiReader.FORMAT_STRING + (if (wsPath == null) "" else wsPath.value)
    path
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

  private def getEndPoint(documentation: Documentation, path: String): DocumentationEndPoint = {
    var ep: DocumentationEndPoint = null

    if (documentation.getApis != null)
      for (endpoint <- asIterable(documentation.getApis)) {
        if (endpoint.path == path) ep = endpoint
      }

    ep match {
      case null => ep = new DocumentationEndPoint(path, apiEndpoint.description); documentation.addApi(ep); ep
      case _ => ep
    }
  }

}

object ApiPropertiesReader {
  private val modelsCache = scala.collection.mutable.Map.empty[Class[_], DocumentationObject]

  def read(hostClass: Class[_]): DocumentationObject = {
    modelsCache.get(hostClass) match {
      case None => val docObj = new ApiModelParser(hostClass).parse; modelsCache += hostClass -> docObj; docObj
      case docObj: Option[DocumentationObject] => docObj.get
      case _ => null
    }
  }

  def readName(hostClass: Class[_]): String = {
    new ApiModelParser(hostClass).readName(hostClass)
  }
}

private class ApiModelParser(val hostClass: Class[_]) extends BaseApiParser {
  private val documentationObject = new DocumentationObject
  private val LOGGER = LoggerFactory.getLogger(classOf[ApiModelParser])

  documentationObject.setName(readName(hostClass))

  private val xmlElementTypeMethod = classOf[XmlElement].getDeclaredMethod("type")

  def readName(hostClass: Class[_]): String = {
    val xmlRootElement = hostClass.getAnnotation(classOf[XmlRootElement])
    val xmlEnum = hostClass.getAnnotation(classOf[XmlEnum])

    if (xmlEnum != null && xmlEnum.value() != null) {
      readName(xmlEnum.value())
    } else if (xmlRootElement != null) {
      if ("##default".equals(xmlRootElement.name())) {
        hostClass.getSimpleName
      } else {
        readString(xmlRootElement.name())
      }
    } else if (hostClass.getName.startsWith("java.lang.")) {
      hostClass.getName.substring("java.lang.".length).toLowerCase
    } else if (hostClass.getName.indexOf(".") < 0) {
      hostClass.getName
    } else {
      LOGGER.error("Class " + hostClass.getName + " is not annotated with a @XmlRootElement annotation, using " + hostClass.getSimpleName)
      hostClass.getSimpleName
    }

  }

  def parse(): DocumentationObject = {
    for (method <- hostClass.getDeclaredMethods) {
      if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()))
        parseMethod(method)
    }

    for (field <- hostClass.getDeclaredFields) {
      if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
        parseField(field)
    }

    documentationObject
  }

  private def parseField(field: Field): Any = {
    parsePropertyAnnotations(field.getName, field.getAnnotations, field.getGenericType, field.getType)
  }

  private def parseMethod(method: Method): Any = {
    if (method.getParameterTypes == null || method.getParameterTypes.length == 0)
      parsePropertyAnnotations(method.getName, method.getAnnotations, method.getGenericReturnType, method.getReturnType)
  }

  private def extractGetterProperty(methodFieldName: String): String = {
    if (methodFieldName != null &&
      (methodFieldName.startsWith("get")) &&
      methodFieldName.length > 3) {
      methodFieldName.substring(3, 4).toLowerCase() + methodFieldName.substring(4, methodFieldName.length())
    }else if (methodFieldName != null &&
      (methodFieldName.startsWith("is")) &&
      methodFieldName.length > 2) {
      methodFieldName.substring(2, 3).toLowerCase() + methodFieldName.substring(3, methodFieldName.length())
    } else {
      null
    }
  }

  def parsePropertyAnnotations(methodFieldName: String, methodAnnotations: Array[Annotation], genericReturnType: Type, returnType: Type): Any = {
    val name = extractGetterProperty(methodFieldName)

    val docParam = new DocumentationParameter
    docParam.required = false;

    var isTransient = false;

    for (ma <- methodAnnotations) {
      ma match {
        case xmlTransient: XmlTransient => {
          isTransient = true;
        };

        case apiProperty: ApiProperty => {
          docParam.description = readString(apiProperty.value)
          docParam.notes = readString(apiProperty.notes)
          try{
            docParam.allowableValues = convertToAllowableValues(apiProperty.allowableValues)
          }catch{
            case e: RuntimeException => LOGGER.error("Allowable values annotation is wrong in for parameter " + docParam.name); e.printStackTrace();
          }
          docParam.paramAccess = readString(apiProperty.access)
        };

        case xmlAttribute: XmlAttribute => {
          docParam.name = readString(xmlAttribute.name, docParam.name, "##default")
          docParam.name = readString(name, docParam.name)
          docParam.required = xmlAttribute.required
        };

        case xmlElement: XmlElement => {
          docParam.name = readString(xmlElement.name, docParam.name, "##default")
          docParam.name = readString(name, docParam.name)
          docParam.defaultValue = readString(xmlElement.defaultValue, docParam.defaultValue, "\u0000")

          docParam.required = xmlElement.required
          val typeValueObj = xmlElementTypeMethod.invoke(xmlElement)
          val typeValue = if (typeValueObj == null) null else typeValueObj.asInstanceOf[Class[_]]
          //          docParam.paramType = readString(if (typeValue != null) typeValue.getName else null, docParam.paramType)
        };

        case xmlElementWrapper: XmlElementWrapper => {
          docParam.wrapperName = readString(xmlElementWrapper.name, docParam.wrapperName, "##default")
        };

        case _ => Unit

      }
    }

    //sometimes transient annotation is defined on property, so while looking at getter and setter make sure there is no transient annotation on property
    if(!isTransient && null != name){
      val propertyAnnotations = this.hostClass.getDeclaredField(name).getAnnotations()
      for( pa <- propertyAnnotations){
        pa match {
          case xmlTransient: XmlTransient => {
            isTransient = true;
          };
          case _ => Unit
        }
      }
    }

    if (docParam.name == null && name != null)
      docParam.name = name;

    if (!isTransient && docParam.name != null) {
      if (docParam.paramType == null) {
        if (TypeUtil.isParameterizedList(genericReturnType)) {
          val parameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
          val valueType = parameterizedType.getActualTypeArguments.head
          docParam.paramType = "List[" + readName(valueType.asInstanceOf[Class[_]]) + "]"
        } else if (TypeUtil.isParameterizedSet(genericReturnType)) {
          val parameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
          val valueType = parameterizedType.getActualTypeArguments.head
          docParam.paramType = "Set[" + readName(valueType.asInstanceOf[Class[_]]) + "]"
        } else if (TypeUtil.isParameterizedMap(genericReturnType)) {
          val parameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
          val typeArgs = parameterizedType.getActualTypeArguments
          val keyType = typeArgs(0)
          val valueType = typeArgs(1)

          val keyName = readName(keyType.asInstanceOf[Class[_]])
          val valueName = readName(valueType.asInstanceOf[Class[_]])
          docParam.paramType = "Map[" + keyName + "," + valueName + "]"
        } else {
          docParam.paramType = readName(genericReturnType.asInstanceOf[Class[_]])
        }
      }

      if (!"void".equals(docParam.paramType))
        documentationObject.addField(docParam)
    }
  }
}