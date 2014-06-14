package com.wordnik.swagger.converter

import com.wordnik.swagger.model.Model

import scala.collection.mutable.HashMap

class TypeConverter extends ModelConverter with BaseConverter {
  val m = new HashMap[String, String]

  def read(cls: Class[_], typeMap: Map[String, String]): Option[Model] = None
  def add(sourceClassName: String, destClassName: String) = {
    m += sourceClassName.toLowerCase -> destClassName
  }

  override def typeMap = m.toMap

  // don't expand mapped models
  override def ignoredClasses: Set[String] = m.keys.toSet
}
