package com.wordnik.swagger.converter

import com.wordnik.swagger.model.Model
import org.slf4j.LoggerFactory

class UUIDConverter extends ModelConverter with BaseConverter {
  private val LOGGER = LoggerFactory.getLogger(this.getClass)

  def read(cls: Class[_], typeMap: Map[String, String]): Option[Model] = None

  override def typeMap = Map("uuid" -> "String")

  override def ignoredClasses: Set[String] = Set("java.util.UUID")
}
