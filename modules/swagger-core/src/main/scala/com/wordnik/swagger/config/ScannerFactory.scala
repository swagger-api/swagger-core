package com.wordnik.swagger.config

object ScannerFactory {
  var scanner: Option[Scanner] = None

  def setScanner(scanner: Scanner) = ScannerFactory.scanner = Option(scanner)
}

trait Scanner {
  def classes(): List[Class[_]]
}
