package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader
import com.wordnik.swagger.config.ConfigFactory
import com.wordnik.swagger.reader._

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet

class DefaultJaxrsConfig extends HttpServlet {
  override def init(servletConfig: ServletConfig) = {
    super.init(servletConfig)

    implicit val config = servletConfig
    ConfigFactory.config = new WebXMLReader()
    ScannerFactory.scanner = Some(new DefaultJaxrsScanner())
    ClassReaders.reader = Some(new DefaultJaxrsApiReader)
  }
}
