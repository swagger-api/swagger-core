package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.config.ConfigFactory

import javax.ws.rs.core.{ Context, Application }

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet

class DefaultJaxrsConfig extends HttpServlet {
  override def init(servletConfig: ServletConfig) = {
    super.init(servletConfig)

    implicit val config = servletConfig
    ConfigFactory.config = new WebXMLReader()
    ScannerFactory.scanner = Some(new DefaultJaxrsScanner())
  }
}