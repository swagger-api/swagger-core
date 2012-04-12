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

import com.wordnik.swagger.core.SwaggerContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect._
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

object TypeUtil {
  /**
   * @return true if the passed type represents a paramterized list
   */
  def isParameterizedList(genericType: Type): Boolean = {
    var isList: Boolean = false
    val isTypeParameterized: Boolean = classOf[ParameterizedType].isAssignableFrom(genericType.getClass)
    if (isTypeParameterized) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      isList = (parameterizedType.getRawType == classOf[java.util.List[_]]) || (parameterizedType.getRawType == classOf[scala.List[_]]) ||
               (parameterizedType.getRawType == classOf[Seq[_]])
    }
    return isList && isTypeParameterized
  }

  /**
   * @return true if the passed type represents a paramterized set
   */
  def isParameterizedSet(genericType: Type): Boolean = {
    var isSet: Boolean = false
    val isTypeParameterized: Boolean = classOf[ParameterizedType].isAssignableFrom(genericType.getClass)
    if (isTypeParameterized) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      isSet = (parameterizedType.getRawType == classOf[java.util.Set[_]]) || (parameterizedType.getRawType == classOf[Set[_]])
    }
    return isSet && isTypeParameterized
  }

  /**
   * @return true if the passed type represents a paramterized map
   */
  def isParameterizedMap(genericType: Type): Boolean = {
    var isMap: Boolean = false
    val isTypeParameterized: Boolean = classOf[ParameterizedType].isAssignableFrom(genericType.getClass)
    if (isTypeParameterized) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      isMap = (parameterizedType.getRawType == classOf[java.util.Map[_, _]]) || (parameterizedType.getRawType == classOf[Map[_, _]])
      if(!isMap)isMap = (parameterizedType.getRawType == classOf[java.util.HashMap[_,_]]);
    }
    return isMap && isTypeParameterized
  }

  /**
   * @return true if the passed type represents a paramterized array
   */
  def isParameterizedArray(genericType: Type): Boolean = {
    var isArray: Boolean = genericType.getClass.isArray
    return isArray
  }

  def isParameterizedScalaOption(genericType: Type): Boolean = {
    var isOption = false
    val isTypeParameterized: Boolean = classOf[ParameterizedType].isAssignableFrom(genericType.getClass)
    if (isTypeParameterized) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      isOption = (parameterizedType.getRawType == classOf[Option[_]])
    }
    return isOption
  }

  /**
   * Gets a parameterized lists types if they are in com.wordnik.* packages
   */
  private def getWordnikParameterTypes(genericType: Type): java.util.List[String] = {
    var list: java.util.List[String] = new java.util.ArrayList[String]
    if (isParameterizedList(genericType) || isParameterizedSet(genericType)) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      for (_listType <- parameterizedType.getActualTypeArguments) {
        checkAndAddConcreteObjectType(_listType, list)
      }
    } else if (isParameterizedMap(genericType)) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      val typeArgs = parameterizedType.getActualTypeArguments
      val keyType = typeArgs(0)
      val valueType = typeArgs(1)
      if (keyType.isInstanceOf[Class[_]]) {
        checkAndAddConcreteObjectType(keyType, list)
      }
      if (valueType.isInstanceOf[Class[_]]) {
        checkAndAddConcreteObjectType(valueType, list)
      }
      list.addAll(getWordnikParameterTypes(keyType))
      list.addAll(getWordnikParameterTypes(valueType))
    } else if (isParameterizedScalaOption(genericType)){
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      for (optionType <- parameterizedType.getActualTypeArguments) {
        checkAndAddConcreteObjectType(optionType, list)
      }
    }
    return list
  }
  
  private def checkAndAddConcreteObjectType(classType:Type, list: java.util.List[String]) {
    if (classType.getClass.isAssignableFrom(classOf[Class[_]])){
      val listType: Class[_] = classType.asInstanceOf[Class[_]]
      if (listType.getName.startsWith(WORDNIK_PACKAGES)) list.add(listType.getName)
    }
  }

  /**
   * Get all classes references by a given list of classes. This includes types of method params and fields
   */
  def getReferencedClasses(classNameList: java.util.List[String]): java.util.Collection[String] = {
    val referencedClasses: java.util.Set[String] = new java.util.HashSet[String]
    import scala.collection.JavaConversions._
    for (className <- classNameList) {
      referencedClasses.addAll(getReferencedClasses(className))
    }
    return referencedClasses
  }

  /**
   * Get all classes references by a given class. This includes types of method params and fields
   */
  def getReferencedClasses(className: String): java.util.Collection[String] = {
    if (REFERENCED_CLASSES_CACHE.containsKey(className)) return REFERENCED_CLASSES_CACHE.get(className)
    else {
      val referencedClasses: java.util.Set[String] = new java.util.HashSet[String]
      if (className.indexOf(".") > 0) {
        referencedClasses.add(className)
        var clazz: Class[_] = null
        try {
          clazz = SwaggerContext.loadClass(className)
        }
        catch {
          case e: Exception => {
            LOGGER.error("Unable to load class " + className)
          }
        }
        if (clazz != null) {
          for (field <- clazz.getFields) {
            if (Modifier.isPublic(field.getModifiers)) {
              var fieldClass: String = field.getType.getName
              var fieldGenericType = field.getGenericType
              field.getType.isArray match {
                case true => {
                  if (!field.getType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl])){
                    fieldClass = field.getType.asInstanceOf[Class[_]].getComponentType.getName
                  }
                }
                case _ =>
              }
              if (fieldClass.startsWith(WORDNIK_PACKAGES)) {
                referencedClasses.add(fieldClass)
              }
              else {
                referencedClasses.addAll(getWordnikParameterTypes(fieldGenericType))
              }
            }
          }
          for (method <- clazz.getMethods) {
            if (Modifier.isPublic(method.getModifiers)) {
              var methodReturnClass: String = method.getReturnType.getName
              var methodGenericType = method.getGenericReturnType

              method.getReturnType.isArray match {
                case true => {
                  if (!method.getReturnType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl])){
                    methodReturnClass = method.getReturnType.asInstanceOf[Class[_]].getComponentType.getName
                  }
                }
                case _ =>
              }
              if (methodReturnClass.startsWith(WORDNIK_PACKAGES)) {
                referencedClasses.add(methodReturnClass)
              }
              else {
                referencedClasses.addAll(getWordnikParameterTypes(methodGenericType))
              }
            }
          }
        }
      }
      REFERENCED_CLASSES_CACHE.put(className, referencedClasses)
      val additionalClasses: java.util.Set[String] = new java.util.HashSet[String]
      import scala.collection.JavaConversions._
      for (referencedClass <- referencedClasses) {
        if (!REFERENCED_CLASSES_CACHE.containsKey(referencedClass)) {
          additionalClasses.addAll(getReferencedClasses(referencedClass))
        }
      }
      referencedClasses.addAll(additionalClasses)
      return referencedClasses
    }
  }

  private final val LOGGER: Logger = LoggerFactory.getLogger(TypeUtil.getClass().getName())
  private final val WORDNIK_PACKAGES: String = "com.wordnik."
  private final val REFERENCED_CLASSES_CACHE: java.util.Map[String, java.util.Set[String]] = new java.util.HashMap[String, java.util.Set[String]]
}


