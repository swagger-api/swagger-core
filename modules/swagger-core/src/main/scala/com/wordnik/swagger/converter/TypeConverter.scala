package com.wordnik.swagger.converter

import scala.collection.mutable.HashMap

class TypeConverter extends ModelConverter with BaseConverter {
  private val LOGGER = LoggerFactory.getLogger(this.getClass)

  val m = new HashMap[String, String]

  def read(cls: Class[_], typeMap: Map[String, String]): Option[Model] = None
  def add(sourceClassName: String, destClassName: String) = {
    m += sourceClassName.toLower -> destClassName
  }

  override def typeMap = m.toMap

  // don't expand mapped models
  override def ignoredClasses: Set[String] = m.keys.toSet
}
