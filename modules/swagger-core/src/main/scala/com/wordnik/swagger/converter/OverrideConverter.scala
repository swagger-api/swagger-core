package com.wordnik.swagger.converter

import com.wordnik.swagger.model._

import org.slf4j.LoggerFactory

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import scala.collection.mutable.HashMap

class OverrideConverter
  extends ModelConverter 
  with BaseConverter {
  private val LOGGER = LoggerFactory.getLogger(classOf[OverrideConverter])
  val overrides = new HashMap[String, Option[Model]]

  implicit val formats = SwaggerSerializers.formats

  def add(classname: String, model: Model) = {
    overrides += classname -> {
      model.qualifiedType match {
        case "" => Some(model.copy(qualifiedType = classname))
        case _ => Option(model)
      }
    }
  }

  def add(classname: String, jsonString: String): Boolean = {
    try {
      add(classname, parse(jsonString).extract[Model])
      true
    }
    catch {
      case e: Exception => {
        LOGGER.error("failed to convert json to model", e)
        false
      }
    }
  }

  def read(cls: Class[_]): Option[Model] = {
    overrides.getOrElse(cls.getName, None)
  }
}
