package com.wordnik.swagger.reader

import com.wordnik.swagger.config._
import com.wordnik.swagger.model._

trait ClassReader {
  def read(docRoot: String, cls: Class[_], config: SwaggerConfig): Option[ApiListing]
}

object ClassReaders {
	var reader: Option[ClassReader] = None

	def setReader(reader: ClassReader) = ClassReaders.reader = Option(reader)
}
