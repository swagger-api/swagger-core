/**
 *  Copyright 2013 Wordnik, Inc.
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
import java.lang.reflect._

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.mutable.{ ListBuffer, HashSet }

object TypeUtil {
  private final val LOGGER: Logger = LoggerFactory.getLogger(TypeUtil.getClass().getName())
  private final val REFERENCED_CLASSES_CACHE: java.util.Map[String, java.util.Set[String]] = new java.util.HashMap[String, java.util.Set[String]]

  val allowablePackages = new HashSet[String]

  def isParameterizedList(gt: Type): Boolean = {
    if (classOf[ParameterizedType].isAssignableFrom(gt.getClass)) {
      val tp = gt.asInstanceOf[ParameterizedType].getRawType
      (tp == classOf[java.util.List[_]] || tp == classOf[scala.List[_]] || tp == classOf[Seq[_]])
    }
    else false
  }

  def isParameterizedSet(genericType: Type): Boolean = {
    if(classOf[ParameterizedType].isAssignableFrom(genericType.getClass)) {
      val tp = genericType.asInstanceOf[ParameterizedType].getRawType
      (tp == classOf[java.util.Set[_]] || tp == classOf[Set[_]])
    }
    else false
  }

  def isParameterizedMap(genericType: Type): Boolean = {
    if(classOf[ParameterizedType].isAssignableFrom(genericType.getClass)) {
      val tp = genericType.asInstanceOf[ParameterizedType].getRawType
      (tp == classOf[java.util.Map[_, _]] || tp == classOf[java.util.HashMap[_, _]] || tp == classOf[Map[_,_]])
    }
    else false
  }

  def isParameterizedArray(genericType: Type): Boolean = genericType.getClass.isArray

  def isParameterizedScalaOption(genericType: Type): Boolean = {
    var isOption = false
    if(classOf[ParameterizedType].isAssignableFrom(genericType.getClass)) {
      val tp = genericType.asInstanceOf[ParameterizedType].getRawType
      (tp == classOf[Option[_]])
    }
    else false
  }

  def getParameterTypes(genericType: Type): List[String] = {
    val lb = new ListBuffer[String]
    if (isParameterizedList(genericType) || isParameterizedSet(genericType)) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      for (_listType <- parameterizedType.getActualTypeArguments) {
        checkAndAddConcreteObjectType(_listType, lb)
      }
    } else if (isParameterizedMap(genericType)) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      val typeArgs = parameterizedType.getActualTypeArguments
      val keyType = typeArgs(0)
      val valueType = typeArgs(1)
      if (keyType.isInstanceOf[Class[_]]) {
        checkAndAddConcreteObjectType(keyType, lb)
      }
      if (valueType.isInstanceOf[Class[_]]) {
        checkAndAddConcreteObjectType(valueType, lb)
      }
      lb ++= getParameterTypes(keyType)
      lb ++= getParameterTypes(valueType)
    } else if (isParameterizedScalaOption(genericType)) {
      val parameterizedType: ParameterizedType = genericType.asInstanceOf[ParameterizedType]
      for (optionType <- parameterizedType.getActualTypeArguments) {
        checkAndAddConcreteObjectType(optionType, lb)
      }
    }
    lb.toList
  }

  private def checkAndAddConcreteObjectType(classType: Type, lb: ListBuffer[String]) {
    if (classType.getClass.isAssignableFrom(classOf[Class[_]])) {
      val listType: Class[_] = classType.asInstanceOf[Class[_]]
      if (isPackageAllowed(listType.getName)) 
        lb += listType.getName
    }
    else {
      classType match {
        case e: ParameterizedTypeImpl => {
          for(t <- e.getActualTypeArguments) {            
            if(t.isInstanceOf[Class[_]]){
              val nm = t.asInstanceOf[Class[_]].getName()
              if(isPackageAllowed(nm)) 
                lb += nm
            }
          }
        }
        case _ =>
      }
    }
  }

  /**
   * Get all classes references by a given list of classes. This includes types of method params and fields
   */
  def getReferencedClasses(classNameList: List[String]): java.util.Collection[String] = {
    val referencedClasses = new java.util.HashSet[String]
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
        } catch {
          case e: Exception => {
            LOGGER.error("Unable to load class " + className)
          }
        }
        if (clazz != null) {
          for (field <- clazz.getFields) {
            if (Modifier.isPublic(field.getModifiers) && !Modifier.isStatic(field.getModifiers)) {
              var fieldClass: String = field.getType.getName
              var fieldGenericType = field.getGenericType
              field.getType.isArray match {
                case true => {
                  if (!field.getType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl])) {
                    fieldClass = field.getType.asInstanceOf[Class[_]].getComponentType.getName
                  }
                }
                case _ =>
              }
              if (isPackageAllowed(fieldClass)) {
                referencedClasses.add(fieldClass)
              } else {
                referencedClasses.addAll(getParameterTypes(fieldGenericType).asJava)
              }
            }
          }
          for (method <- clazz.getMethods) {
            if (Modifier.isPublic(method.getModifiers) && !Modifier.isStatic(method.getModifiers)) {
              var methodReturnClass: String = method.getReturnType.getName
              var methodGenericType = method.getGenericReturnType

              method.getReturnType.isArray match {
                case true => {
                  if (!method.getReturnType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl])) {
                    methodReturnClass = method.getReturnType.asInstanceOf[Class[_]].getComponentType.getName
                  }
                }
                case _ =>
              }
              if (isPackageAllowed(methodReturnClass)) {
                referencedClasses.add(methodReturnClass)
              } else {
                referencedClasses.addAll(getParameterTypes(methodGenericType).asJava)
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

  val packagesToSkip = Set("scala", "byte", "char", "java", "int", "long", "String", "boolean", "void", "[Ljava")

  def isPackageAllowed(str: String): Boolean = {
    var isOk = false
    allowablePackages.size match {
      case 0 => {
        isOk = true
        packagesToSkip.foreach(a => if (str.startsWith(a)) isOk = false)
      }
      case _ => {
        isOk = false
        allowablePackages.foreach(a => if (str.startsWith(a)) isOk = true)
      }
    }
    isOk
  }

  def addAllowablePackage(p: String) = Option(p).map(allowablePackages += p)

}
