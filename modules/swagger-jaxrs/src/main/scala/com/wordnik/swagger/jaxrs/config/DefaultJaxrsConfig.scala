package com.wordnik.swagger.jaxrs.config


import com.wordnik.swagger.config.{ ConfigFactory, ScannerFactory }
import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.reader._

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet

class DefaultJaxrsConfig extends HttpServlet {
  override def init(servletConfig: ServletConfig) = {
    super.init(servletConfig)

    implicit val config = servletConfig
    ConfigFactory.config = new WebXMLReader()
    ScannerFactory.scanner = Some(new DefaultJaxrsScanner())
    servletConfig.getInitParameter("scan.all.resources") match {
      case "true" => {
        val reader = new BasicJaxrsReader
        Option(servletConfig.getInitParameter("ignore.routes")) match {
          case Some(e) => reader.ignoredRoutes = e.split(",").toSet
          case _ =>
        }
        ClassReaders.reader = Some(reader)
      }
      case _ => ClassReaders.reader = Some(new DefaultJaxrsApiReader)
    }
  }
}
