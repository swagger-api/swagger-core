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
import java.util._

object TypeUtil {
  /**
   * @return true if the passed type represents a paramterized list
   */
  def isParameterizedList(genericType: Type): Boolean = {
    var isList: Boolean = false
    val isTypeParameterized: Boolean = classOf[ParameterizedType].isAssignableFrom(genericType.getClass)
    if (isTypeParameterized) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      isList = (parameterizedType.getRawType == classOf[List[_]])
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
      isSet = (parameterizedType.getRawType == classOf[Set[_]])
    }
    return isSet && isTypeParameterized
  }

  /**
   * @return true if the passed type represents a paramterized map
   */
  def isParameterizedMap(genericType: Type): Boolean = {
    var isList: Boolean = false
    val isTypeParameterized: Boolean = classOf[ParameterizedType].isAssignableFrom(genericType.getClass)
    if (isTypeParameterized) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      isList = (parameterizedType.getRawType == classOf[Map[_, _]])
    }
    return isList && isTypeParameterized
  }

  /**
   * @return true if the passed type represents a paramterized array
   */
  def isParameterizedArray(genericType: Type): Boolean = {
    var isArray: Boolean = genericType.getClass.isArray
    return isArray
  }

  /**
   * Gets a parameterized lists types if they are in com.wordnik.* packages
   */
  private def getWordnikParameterTypes(genericType: Type): List[String] = {
    var list: List[String] = new ArrayList[String]
    if (isParameterizedList(genericType)) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      for (_listType <- parameterizedType.getActualTypeArguments) {
        val listType: Class[_] = _listType.asInstanceOf[Class[_]]
        if (listType.getName.startsWith(WORDNIK_PACKAGES)) list.add(listType.getName)
      }
    }
    return list
  }

  /**
   * Get all classes references by a given list of classes. This includes types of method params and fields
   */
  def getReferencedClasses(classNameList: List[String]): Collection[String] = {
    val referencedClasses: Set[String] = new HashSet[String]
    import scala.collection.JavaConversions._
    for (className <- classNameList) {
      referencedClasses.addAll(getReferencedClasses(className))
    }
    return referencedClasses
  }

  /**
   * Get all classes references by a given class. This includes types of method params and fields
   */
  def getReferencedClasses(className: String): Collection[String] = {
    if (REFERENCED_CLASSES_CACHE.containsKey(className)) return REFERENCED_CLASSES_CACHE.get(className)
    else {
      val referencedClasses: Set[String] = new HashSet[String]
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
          for (field <- clazz.getDeclaredFields) {
            if (Modifier.isPublic(field.getModifiers)) {
              var fieldClass: String = field.getType.getName
              if (fieldClass.startsWith(WORDNIK_PACKAGES)) {
                referencedClasses.add(fieldClass)
              }
              else {
                referencedClasses.addAll(getWordnikParameterTypes(field.getGenericType))
              }
            }
          }
          for (method <- clazz.getDeclaredMethods) {
            if (Modifier.isPublic(method.getModifiers)) {
              var methodReturnClass: String = method.getReturnType.getName
              if (methodReturnClass.startsWith(WORDNIK_PACKAGES)) {
                referencedClasses.add(methodReturnClass)
              }
              else {
                referencedClasses.addAll(getWordnikParameterTypes(method.getGenericReturnType))
              }
            }
          }
        }
      }
      REFERENCED_CLASSES_CACHE.put(className, referencedClasses)
      val additionalClasses: Set[String] = new HashSet[String]
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
  private final val REFERENCED_CLASSES_CACHE: Map[String, Set[String]] = new HashMap[String, Set[String]]
}


