package com.wordnik.swagger.servlet.config

import com.wordnik.swagger.config._
import com.wordnik.swagger.reader._

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet

class DefaultServletReaderConfig extends HttpServlet {
  override def init(servletConfig: ServletConfig) = {
    super.init(servletConfig)

    implicit val config = servletConfig
    val webConfig = new WebXMLReader
    ConfigFactory.config = webConfig

    val scanner = new ServletScanner
    ScannerFactory.setScanner(scanner)
    scanner.setResourcePackage(webConfig.resourcePackage)
    ClassReaders.reader = Some(new ServletReader )
  }
}
