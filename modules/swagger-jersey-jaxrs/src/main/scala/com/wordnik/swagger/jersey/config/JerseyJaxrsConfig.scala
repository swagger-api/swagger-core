package com.wordnik.swagger.jersey.config

import com.wordnik.swagger.jaxrs.config._

import com.wordnik.swagger.jersey.JerseyApiReader
import com.wordnik.swagger.config.{ ConfigFactory, ScannerFactory }
import com.wordnik.swagger.reader._

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet

class JerseyJaxrsConfig extends HttpServlet {
  override def init(servletConfig: ServletConfig) = {
    super.init(servletConfig)

    implicit val config = servletConfig
    ConfigFactory.config = new WebXMLReader()
    ScannerFactory.scanner = Some(new DefaultJaxrsScanner())
    ClassReaders.reader = Some(new JerseyApiReader)
  }
}
