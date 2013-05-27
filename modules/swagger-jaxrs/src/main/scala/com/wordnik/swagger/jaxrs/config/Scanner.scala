package com.wordnik.swagger.jaxrs.config

object ScannerFactory {
	var scanner: Option[Scanner] = None
}

trait Scanner {
	def classes(): List[Class[_]]
}
