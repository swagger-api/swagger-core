package com.wordnik.swagger.converter

import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations.ApiModel

import org.slf4j.LoggerFactory

import scala.collection.mutable.LinkedHashMap

class SwaggerSchemaConverter 
  extends ModelConverter 
  with BaseConverter {

  def read(cls: Class[_]): Option[Model] = {
    Option(cls).flatMap({
      cls => {
        implicit val properties = new LinkedHashMap[String, ModelProperty]()
        new ModelPropertyParser(cls).parse

        val p = (for((key, value) <- properties) 
          yield (value.position, key, value)
        ).toList

        val sortedProperties = new LinkedHashMap[String, ModelProperty]()
        p.sortWith(_._1 < _._1).foreach(e => sortedProperties += e._2 -> e._3)

        val baseModel = Option(cls.getAnnotation(classOf[ApiModel])) match {
          case Some(e) => Option(e.parent.getName)
          case _ => None
        }
        val discriminator = Option(cls.getAnnotation(classOf[ApiModel])) match {
          case Some(e) => Option(e.discriminator)
          case _ => None
        }

        sortedProperties.size match {
          case 0 => None
          case _ => Some(Model(
            toName(cls),
            toName(cls),
            cls.getName,
            sortedProperties,
            toDescriptionOpt(cls),
            baseModel,
            discriminator
          ))
        }
      }
    })
  }
}