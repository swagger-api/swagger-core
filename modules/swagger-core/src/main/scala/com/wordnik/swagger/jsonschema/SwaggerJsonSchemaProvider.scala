package com.wordnik.swagger.jsonschema

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.ReflectionUtil
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util.{ TypeUtil, JsonUtil }
import com.wordnik.swagger.core._

import org.slf4j.LoggerFactory

import com.fasterxml.jackson.databind.node._

import java.lang.reflect.{ Type, Field, Modifier, Method }
import java.lang.annotation.Annotation
import javax.xml.bind.annotation._

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

import scala.collection.JavaConverters._

class SwaggerJsonSchemaProvider extends JsonSchemaProvider {
  def read(hostClass: Class[_]): DocumentationObject = {
    new ApiModelParser(hostClass).parse()
  }
}

class ApiModelParser(val hostClass: Class[_]) extends BaseApiParser {
  private val documentationObject = new DocumentationObject
  private val LOGGER = LoggerFactory.getLogger(classOf[ApiModelParser])
  var hasAccessorNoneAnnotation = false;
  documentationObject.setName(readName(hostClass))

  private val xmlElementTypeMethod = classOf[XmlElement].getDeclaredMethod("type")
  private val processedFields: java.util.List[String] = new java.util.ArrayList[String]()

  def readName(hostClass: Class[_], isSimple: Boolean = true): String = {
    val xmlRootElement = hostClass.getAnnotation(classOf[XmlRootElement])
    val xmlEnum = hostClass.getAnnotation(classOf[XmlEnum])

    val accessorNone = hostClass.getAnnotation(classOf[XmlAccessorType]).asInstanceOf[XmlAccessorType]
    if (null != accessorNone && (accessorNone.value() == XmlAccessType.NONE)) hasAccessorNoneAnnotation = true;

    val name = {
      if (xmlEnum != null && xmlEnum.value() != null) {
        readName(xmlEnum.value())
      } else if (xmlRootElement != null) {
        if ("##default".equals(xmlRootElement.name())) {
          if (isSimple) hostClass.getSimpleName else hostClass.getName
        } else {
          if (isSimple) readString(xmlRootElement.name()) else hostClass.getName
        }
      } else if (hostClass.getName.startsWith("java.lang.")) {
        hostClass.getName.substring("java.lang.".length)
      } else if (hostClass.getName.indexOf(".") < 0) {
        hostClass.getName
      } else {
        LOGGER.info("Class " + hostClass.getName + " is not annotated with a @XmlRootElement annotation, using " + hostClass.getSimpleName)
        if (isSimple) hostClass.getSimpleName else hostClass.getName
      }
    }
    validateDatatype(name)
  }

  def parse(): DocumentationObject = {
    parseRecurrsive(hostClass);
    documentationObject
  }

  /**
   * Parse methods from the class and all of its parent classes
   */
  def parseRecurrsive(hostClass: Class[_]): Unit = {
    if (null != hostClass) {
      for (method <- hostClass.getDeclaredMethods) {
        if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()))
          parseMethod(method)
      }

      for (field <- hostClass.getDeclaredFields) {
        if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
          parseField(field)
      }
      parseRecurrsive(hostClass.getSuperclass)
    }
  }

  private def parseField(field: Field): Any = {
    parsePropertyAnnotations(field.getName, field.getAnnotations, field.getGenericType, field.getType)
  }

  private def parseMethod(method: Method): Any = {
    if (method.getParameterTypes == null || method.getParameterTypes.length == 0)
      parsePropertyAnnotations(method.getName, method.getAnnotations, method.getGenericReturnType, method.getReturnType)
  }

  private def extractGetterProperty(methodFieldName: String): (String, Boolean) = {
    if (methodFieldName != null &&
      (methodFieldName.startsWith("get")) &&
      methodFieldName.length > 3) {
      (methodFieldName.substring(3, 4).toLowerCase() + methodFieldName.substring(4, methodFieldName.length()), true)
    } else if (methodFieldName != null &&
      (methodFieldName.startsWith("is")) &&
      methodFieldName.length > 2) {
      (methodFieldName.substring(2, 3).toLowerCase() + methodFieldName.substring(3, methodFieldName.length()), true)
    } else {
      (methodFieldName, false)
    }
  }

  def parsePropertyAnnotations(methodFieldName: String, methodAnnotations: Array[Annotation], genericReturnType: Type, returnType: Type): Any = {
    val (name, isGetter) = extractGetterProperty(methodFieldName)

    val docParam = new DocumentationParameter
    docParam.required = false;

    var isTransient = false;
    var isXmlElement = false;
    var isFieldExists = false;

    var methodAnnoOutput = processAnnotations(name, methodAnnotations, docParam)
    isTransient = methodAnnoOutput._1
    isXmlElement = methodAnnoOutput._2
    val isDocumented = methodAnnoOutput._3

    try {
      val propertyAnnotations = getDeclaredField(this.hostClass, name).getAnnotations()
      var propAnnoOutput = processAnnotations(name, propertyAnnotations, docParam)
      isFieldExists = true;
      if (!isXmlElement) { isXmlElement = propAnnoOutput._2 }
      if (!isTransient) { isTransient = propAnnoOutput._1 }
    } catch {
      //this means there is no field declared to look for field level annotations.
      case e: java.lang.NoSuchFieldException => isTransient = false
    }

    if (docParam.name == null && name != null)
      docParam.name = name;

    //if class has accessor none annotation, the method/field should have explicit xml element annotations, if not
    // consider it as transient
    if (!isXmlElement && hasAccessorNoneAnnotation) {
      isTransient = true;
    }

    if (!(isTransient && !isXmlElement) && docParam.name != null && (isFieldExists || isGetter || isDocumented)) {
      if (docParam.paramType == null) {
        docParam.paramType = ApiPropertiesReader.getDataType(genericReturnType, returnType)
      }
      if (!"void".equals(docParam.paramType) && null != docParam.paramType && !processedFields.contains(docParam.getName()))
        documentationObject.addField(docParam)
      processedFields.add(docParam.getName())
      validateParam(docParam)
    }
  }

  def validateParam(param: DocumentationParameter) =
    param.dataType = validateDatatype(param.dataType)

  def validateDatatype(datatype: String): String = {
    datatype match {
      case "Byte" => "byte"
      case "Boolean" => "boolean"
      case "Integer" => "int"
      case "Long" => "long"
      case "Float" => "float"
      case "Double" => "double"
      case "String" => "string"
      case _ => datatype
    }
  }

  /**
   * Incase of subclass and superclass scenario, for properties defined at base class we need to get the super class
   * and find the fields.
   */
  private def getDeclaredField(inputClass: Class[_], fieldName: String): Field = {
    try {
      return inputClass.getDeclaredField(fieldName)
    } catch {
      case t: NoSuchFieldException => {
        if (inputClass.getSuperclass != null && inputClass.getSuperclass.getName != "Object") {
          return getDeclaredField(inputClass.getSuperclass, fieldName)
        } else {
          throw t;
        }
      }
    }
  }

  private def processAnnotations(name: String, annotations: Array[Annotation], docParam: DocumentationParameter): (Boolean, Boolean, Boolean) = {
    var isTransient = false
    var isXmlElement = false
    var isDocumented = false
    for (ma <- annotations) {
      ma match {
        case xmlTransient: XmlTransient => {
          isTransient = true
        }
        case apiProperty: ApiProperty => {
          docParam.description = readString(apiProperty.value)
          docParam.notes = readString(apiProperty.notes)
          docParam.paramType = readString(apiProperty.dataType)
          isDocumented = true

          try {
            docParam.allowableValues = convertToAllowableValues(apiProperty.allowableValues)
          } catch {
            case e: RuntimeException => LOGGER.error("Allowable values annotation is wrong in for parameter " + docParam.name); e.printStackTrace();
          }
          docParam.paramAccess = readString(apiProperty.access)
        }
        case xmlAttribute: XmlAttribute => {
          docParam.name = readString(xmlAttribute.name, docParam.name, "##default")
          docParam.name = readString(name, docParam.name)
          docParam.required = xmlAttribute.required
          isXmlElement = true
        }
        case xmlElement: XmlElement => {
          docParam.name = readString(xmlElement.name, docParam.name, "##default")
          docParam.name = readString(name, docParam.name)
          docParam.defaultValue = readString(xmlElement.defaultValue, docParam.defaultValue, "\u0000")

          docParam.required = xmlElement.required
          val typeValueObj = xmlElementTypeMethod.invoke(xmlElement)
          val typeValue = if (typeValueObj == null) null else typeValueObj.asInstanceOf[Class[_]]
          isXmlElement = true
          // docParam.paramType = readString(if (typeValue != null) typeValue.getName else null, docParam.paramType)
        }
        case xmlElementWrapper: XmlElementWrapper => {
          docParam.wrapperName = readString(xmlElementWrapper.name, docParam.wrapperName, "##default")
        }
        case _ => Unit
      }
    }
    (isTransient, isXmlElement, isDocumented)
  }
}

