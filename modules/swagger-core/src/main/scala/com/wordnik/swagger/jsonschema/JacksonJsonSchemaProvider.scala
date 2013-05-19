package com.wordnik.swagger.jsonschema

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.ReflectionUtil
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util.{ TypeUtil, JsonUtil }
import com.wordnik.swagger.core._

import org.slf4j.LoggerFactory

import com.fasterxml.jackson.databind.node._

import java.util.ArrayList
import java.lang.reflect.{ Type, Field, Modifier, Method }
import java.lang.annotation.Annotation
import javax.xml.bind.annotation._

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonProperty}

import scala.collection.mutable.{ListBuffer, LinkedHashMap}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class JacksonJsonSchemaProvider extends JsonSchemaProvider {
	import com.fasterxml.jackson.module.scala._
	import com.fasterxml.jackson.databind.ObjectMapper
	import com.fasterxml.jackson.databind.jsonSchema.factories.SchemaFactoryWrapper

	def read(cls: Class[_]): DocumentationObject = {
    if(cls != null){
    	val o = new ObjectMapper
    	val visitor = new SchemaFactoryWrapper
    	o.registerModule(new DefaultScalaModule)
    	o.acceptJsonFormatVisitor(o.constructType(cls), visitor)
    	val schema = visitor.finalSchema.asObjectSchema
    	val properties = new ArrayList[DocumentationParameter]

    	for((name, prop) <- schema.getProperties.asScala) {
        val description = ""
        val notes = ""
        val paramType = validateDataType(prop.getType.value())
        val defaultValue = ""
        val allowableValues = null
        val required = false
        val allowMultiple = false

        if(!ApiPropertiesReader.excludedFieldTypes.contains(name)){
          val property = new DocumentationParameter(
            name,
            description,
            notes,
            paramType,
            defaultValue,
            allowableValues,
            required,
            allowMultiple)
      		properties.add(property)
        }
    	}

    	val output = new DocumentationObject
    	output.setName(extractName(cls))
      output.setFields(properties)      	

      output
		}
		else null
	}

  def validateDataType(datatype: String): String = {
    datatype.toLowerCase match {
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

  def extractName(cls: Class[_]): String = {
  	import javax.xml.bind.annotation._

    val xmlRootElement = cls.getAnnotation(classOf[XmlRootElement])
    val xmlEnum = cls.getAnnotation(classOf[XmlEnum])

    val hasAccessorNoneAnnotation = Option(cls.getAnnotation(classOf[XmlAccessorType]).asInstanceOf[XmlAccessorType]) match {
    	case Some(v) if (v.value == XmlAccessType.NONE) => false
    	case _ => true
    }

	  if (xmlEnum != null && xmlEnum.value != null) {
      extractName(xmlEnum.value())
    } else if (xmlRootElement != null) {
      if ("##default".equals(xmlRootElement.name())) {
        cls.getSimpleName 
      } else {
        xmlRootElement.name() 
      }
    } else if (cls.getName.startsWith("java.lang.")) {
      cls.getName.substring("java.lang.".length)
    } else if (cls.getName.indexOf(".") < 0) {
      cls.getName
    } else {
      cls.getSimpleName 
    }
  }
}