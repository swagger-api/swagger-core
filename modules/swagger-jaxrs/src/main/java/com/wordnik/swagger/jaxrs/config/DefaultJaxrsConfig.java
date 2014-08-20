package com.wordnik.swagger.jaxrs.config;


// import com.wordnik.swagger.config.*;
import javax.ws.rs.core.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class DefaultJaxrsConfig extends HttpServlet {
  @Override
  public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
    super.init(servletConfig);

    // @Context Application app;

    // System.out.println(app);

/*
    implicit val config = servletConfig
    ConfigFactory.config = new WebXMLReader()
    ScannerFactory.scanner = Some(new DefaultJaxrsScanner())
    if(servletConfig.getInitParameter("scan.all.resources") != null) {
      if(app != null)
        (app.getClasses().asScala ++ app.getSingletons().asScala.map(ref => ref.getClass)).toList



    } match {
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
    */
  }
}
