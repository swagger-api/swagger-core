package com.wordnik.swagger.converter

import com.wordnik.swagger.model._

import org.slf4j.LoggerFactory

class JodaDateTimeConverter extends ModelConverter with BaseConverter {
  private val LOGGER = LoggerFactory.getLogger(this.getClass)

  def read(cls: Class[_], typeMap: Map[String, String]): Option[Model] = None

  // map DateTime to Date, which is serialized as such:
  //
  // {
  //   "type": "string",
  //   "format": "date-time"
  // }
  // 
  // (note the keys are lowercase when matching)
  override def typeMap = Map("datetime" -> "Date")

  // don't expand DateTime models
  override def ignoredClasses: Set[String] = Set("org.joda.time.DateTime")
}