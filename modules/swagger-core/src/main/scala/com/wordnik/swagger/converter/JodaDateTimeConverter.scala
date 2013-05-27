package com.wordnik.swagger.converter

import com.wordnik.swagger.model._

import org.slf4j.LoggerFactory

class JodaDateTimeConverter
  extends ModelConverter
  with BaseConverter {

  def read(cls: Class[_]): Option[Model] = None

  // don't expand DateTime models
  override def ignoredClasses: Set[String] = Set("org.joda.time.DateTime")
}