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
import com.wordnik.swagger.core.util.ReflectionUtil
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util.{ TypeUtil, JsonUtil }
import com.wordnik.swagger.jsonschema._
import org.slf4j.LoggerFactory

import com.fasterxml.jackson.databind.node._

import java.lang.reflect.{ Type, Field, Modifier, Method }
import java.lang.annotation.Annotation
import javax.xml.bind.annotation._

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

import scala.collection.JavaConverters._

object ApiPropertiesReader {
  var schemaProvider: JsonSchemaProvider = new SwaggerJsonSchemaProvider

  private val modelsCache = scala.collection.mutable.Map.empty[Class[_], DocumentationObject]

  def read(hostClass: Class[_]): DocumentationObject = {
    modelsCache.get(hostClass) match {
      case None => {
        !hostClass.isEnum && !hostClass.getName.startsWith("java.lang.") match {
          case true => {
            val docObj = schemaProvider.read(hostClass)
            modelsCache += hostClass -> docObj
            docObj
          }
          case _ => null
        }
      }
      case docObj: Option[DocumentationObject] => docObj.get
      case _ => null
    }
  }

  def readName(hostClass: Class[_], isSimple: Boolean = true): String = {
    new ApiModelParser(hostClass).readName(hostClass, isSimple)
  }

  def getDataType(genericReturnType: Type, returnType: Type): String = {
    var paramType: String = null
    if (TypeUtil.isParameterizedList(genericReturnType)) {
      val parameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
      val valueType = parameterizedType.getActualTypeArguments.head
      paramType = "List[" + getDataType(valueType, valueType) + "]"
    } else if (TypeUtil.isParameterizedSet(genericReturnType)) {
      val parameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
      val valueType = parameterizedType.getActualTypeArguments.head
      paramType = "Set[" + getDataType(valueType, valueType) + "]"
    } else if (TypeUtil.isParameterizedMap(genericReturnType)) {
      val parameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
      val typeArgs = parameterizedType.getActualTypeArguments
      val keyType = typeArgs(0)
      val valueType = typeArgs(1)

      val keyName: String = getDataType(keyType, keyType)
      val valueName: String = getDataType(valueType, valueType)
      paramType = "Map[" + keyName + "," + valueName + "]"
    } else if (!returnType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl]) && returnType.asInstanceOf[Class[_]].isArray) {
      var arrayClass = returnType.asInstanceOf[Class[_]].getComponentType
      paramType = "Array[" + arrayClass.getSimpleName + "]"
    } else {
      //we might also have properties that are parametarized by not assignable to java collections. Examples: Scala collections
      ///This step will ignore all those fields.
      if (!genericReturnType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl])) {
        paramType = readName(genericReturnType.asInstanceOf[Class[_]])
      } else {
        //handle scala options
        val parameterizedType: java.lang.reflect.ParameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
        if (parameterizedType.getRawType == classOf[Option[_]]) {
          val valueType = parameterizedType.getActualTypeArguments.head
          paramType = getDataType(valueType, valueType)
        }
      }
    }
    paramType
  }

  def getGenericTypeParam(genericReturnType: Type, returnType: Type): String = {
    var typeParam: String = null
    if (TypeUtil.isParameterizedList(genericReturnType) ||
      TypeUtil.isParameterizedSet(genericReturnType)) {
      val parameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
      val valueType = parameterizedType.getActualTypeArguments.head
      typeParam = readName(valueType.asInstanceOf[Class[_]], false)
    } else if (TypeUtil.isParameterizedMap(genericReturnType)) {
      val parameterizedType = genericReturnType.asInstanceOf[java.lang.reflect.ParameterizedType]
      val typeArgs = parameterizedType.getActualTypeArguments
      val keyType = typeArgs(0)
      val valueType = typeArgs(1)
      val keyName = readName(keyType.asInstanceOf[Class[_]], false)
      val valueName = readName(valueType.asInstanceOf[Class[_]], false)
      typeParam = "Map[" + keyName + "," + valueName + "]"
    } else if (!returnType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl]) && returnType.asInstanceOf[Class[_]].isArray) {
      var arrayClass = returnType.asInstanceOf[Class[_]].getComponentType
      typeParam = arrayClass.getName
    } else {
      //we might also have properties that are parametarized by not assignable to java collections. Examples: Scala collections
      ///This step will ignore all those fields.
      if (!genericReturnType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl])) {
        typeParam = readName(genericReturnType.asInstanceOf[Class[_]], false)
      }
    }
    typeParam
  }
}
