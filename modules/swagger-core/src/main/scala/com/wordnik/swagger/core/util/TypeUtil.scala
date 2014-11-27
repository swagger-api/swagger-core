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
  private final val logger: Logger = LoggerFactory.getLogger(TypeUtil.getClass().getName())
  private final val classCache: java.util.Map[String, java.util.Set[String]] = new java.util.HashMap[String, java.util.Set[String]]

  val allowablePackages = new HashSet[String]

  /**
   * Finds the name of all classes referenced in the classnameList which are not
   * in the supplied set of refs
   **/
  def getReferencedClasses(classnameList: List[String], refs: HashSet[String] = new HashSet[String]): Set[String] = {
    (for(classname <- classnameList) 
      yield getReferencedClasses(classname, refs)
    ).flatMap(x => x).toSet
  }

  /**
   * Finds the name of all classes referenced in the classname which are not
   * in the supplied set of refs
   **/
  def getReferencedClasses(classname: String, refs: HashSet[String]): Set[String] = {
    val regex = """[\w]*\[(.*?)\]""".r
    val cls = classname match {
      case regex(inner) => inner
      case _ => classname
    }
    classCache.asScala.getOrElseUpdate(cls, {
      (updateReferencedClasses(cls, refs)).asJava
    }).asScala.toSet
  }

  def isOptionalType(gt: Type): Boolean = {
    if (classOf[ParameterizedType].isAssignableFrom(gt.getClass)) {
      val tp = gt.asInstanceOf[ParameterizedType].getRawType
      (tp == classOf[Option[_]] || tp.asInstanceOf[Class[_]].getSimpleName.equals("Optional"))
    }
    else false
  }

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

  private def getParameterTypes(genericType: Type): List[String] = {
    val lb = new ListBuffer[String]
    if(genericType.isInstanceOf[ParameterizedType]) {
      val parameterizedType = genericType.asInstanceOf[ParameterizedType]
      if (isParameterizedList(genericType) || isParameterizedSet(genericType)) {
        for (listType <- parameterizedType.getActualTypeArguments)
          lb ++= extractConcreteObjectTypes(listType)
      } else if (isParameterizedMap(genericType)) {
        val typeArgs = parameterizedType.getActualTypeArguments
        val keyType = typeArgs(0)
        val valueType = typeArgs(1)
        if (keyType.isInstanceOf[Class[_]])
          lb ++= extractConcreteObjectTypes(keyType)
        if (valueType.isInstanceOf[Class[_]])
          lb ++= extractConcreteObjectTypes(valueType)
        lb ++= getParameterTypes(keyType)
        lb ++= getParameterTypes(valueType)
      } else if (isParameterizedScalaOption(genericType)) {
        for (optionType <- parameterizedType.getActualTypeArguments) {
          lb ++= extractConcreteObjectTypes(optionType)
        }
      }
    }
    lb.toList
  }

  private def extractConcreteObjectTypes(classType: Type): List[String] = {
    val output = new ListBuffer[String]

    if (classType.getClass.isAssignableFrom(classOf[Class[_]])) {
      val listType: Class[_] = classType.asInstanceOf[Class[_]]
      if (isPackageAllowed(listType.getName)) {
        output += listType.getName
      }
    }
    else {
      classType match {
        case e: ParameterizedTypeImpl => {
          for(t <- e.getActualTypeArguments) {
            if(t.isInstanceOf[Class[_]]){
              val nm = t.asInstanceOf[Class[_]].getName()
              if(isPackageAllowed(nm)) 
                output += nm
            }
          }
        }
        case _ =>
      }
    }
    output.toList
  }

  /** 
    * recursive function to add class references from fields, etc.
   **/ 
  private def updateReferencedClasses(cls: String, refs: HashSet[String]): Set[String] = {
    try {
      val loadedClass = SwaggerContext.loadClass(cls)
      refs += cls
      val refsToExpand = (referencesInFields(loadedClass) ++ referencesInMethods(loadedClass)) -- refs.toSet
      refsToExpand.foreach(ref => {
        updateReferencedClasses(ref, refs)
        refs += ref
      })
    } catch {
      case e: Exception => logger.error("Unable to load class " + cls)
    }    
    refs.toSet
  }

  private def referencesInFields(cls: Class[_]): Set[String] = {
    (for (field <- cls.getFields) yield {
      if (Modifier.isPublic(field.getModifiers) && !Modifier.isStatic(field.getModifiers)) {
        val fieldClass = {
          if(field.getType.isArray) {
            if (!field.getType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl]))
              field.getType.asInstanceOf[Class[_]].getComponentType.getName
            else field.getType.getName
          }
          else field.getType.getName
        }
        if (isPackageAllowed(fieldClass)) Set(fieldClass)
        else {
          getParameterTypes(field.getGenericType).toSet
        }
      }
      else Set[String]().empty
    }).flatten.toSet
  }

  private def referencesInMethods(cls: Class[_]): Set[String] = {
    (for (method <- cls.getMethods) yield {
      if (Modifier.isPublic(method.getModifiers) && !Modifier.isStatic(method.getModifiers)) {
        val methodReturnClass: String = {
          if(method.getReturnType.isArray) {
            if (!method.getReturnType.getClass.isAssignableFrom(classOf[ParameterizedTypeImpl]))
              method.getReturnType.asInstanceOf[Class[_]].getComponentType.getName
            else method.getReturnType.getName
          }
          else method.getReturnType.getName
        }
        if (isPackageAllowed(methodReturnClass)) Set(methodReturnClass)
        else getParameterTypes(method.getGenericReturnType)
      }
      else Set[String]().empty
    }).flatten.toSet
  }

  val packagesToSkip = Set("scala", "byte", "char", "java", "int", "long", "String", "boolean", "void", "[Ljava")

  private def isPackageAllowed(str: String): Boolean = {
    if(allowablePackages.size > 0) {
      (for(name <- allowablePackages) yield {
        if(str.startsWith(name)) Some(true)
        else None
      }).flatten.size > 0
    }
    else {
      (for(name <- packagesToSkip) yield {
        if(str.startsWith(name)) Some(true)
        else None
      }).flatten.size == 0
    }
  }

  def addAllowablePackage(p: String) = Option(p).map(allowablePackages += p)
}
