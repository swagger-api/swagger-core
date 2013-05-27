package com.wordnik.swagger.converter

import com.wordnik.swagger.model._

import com.fasterxml.jackson.module.scala._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsonSchema.factories.SchemaFactoryWrapper
import com.fasterxml.jackson.databind.jsonSchema.types._


import java.lang.reflect.{ Type, Field, Modifier, Method }
import java.lang.annotation.Annotation

import scala.collection.JavaConverters._
import scala.collection.mutable.{ ListBuffer, LinkedHashMap }

class JsonSchemaConverter 
  extends ModelConverter 
  with BaseConverter {
  def read(cls: Class[_]): Option[Model] = {
    Option(cls).flatMap({
      cls => {
        val o = new ObjectMapper
        val visitor = new SchemaFactoryWrapper
        o.registerModule(new DefaultScalaModule)
        o.acceptJsonFormatVisitor(o.constructType(cls), visitor)
        visitor.finalSchema match {
          case schema: ObjectSchema => {
            val properties = new LinkedHashMap[String, ModelProperty]
            for((name, prop) <- schema.getProperties.asScala) {
              properties += name -> ModelProperty(
                prop.getType.name,
                prop.getType.name, // this should be the fully-qualified name
                0,
                Option(prop.getRequired).map(_.toString.toBoolean).orElse(Some(false)).get,
                Some(""),
                AnyAllowableValues,
                None
              )
            }
            Some(
              Model(
                toName(cls),
                toName(cls),
                cls.getName,
                properties,
                Option(schema.getDescription)
              )
            )
          }
          case e: JsonSchema => {
            println("unexpected type " + e)
            None
          }
        }
      }
    })
  }

  def readAll(cls: Class[_]): List[Model] = List()
}