package com.wordnik.swagger.reader

import com.wordnik.swagger.config._
import com.wordnik.swagger.model._

import java.lang.reflect.Method

import scala.collection.mutable.ListBuffer

trait ClassReader {
  def read(docRoot: String, parentPath: String, cls: Class[_], config: SwaggerConfig,
           operations: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]],
           parentMethods: ListBuffer[Method]): Option[ApiListing]
}

object ClassReaders {
	var reader: Option[ClassReader] = None

	def setReader(reader: ClassReader) = ClassReaders.reader = Option(reader)
}
