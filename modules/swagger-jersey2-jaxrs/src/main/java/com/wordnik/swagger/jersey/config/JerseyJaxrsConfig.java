package com.wordnik.swagger.jersey.config;

import com.wordnik.swagger.jaxrs.config.*;

import javax.ws.rs.core.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class JerseyJaxrsConfig extends HttpServlet {
  @Override
  public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
    super.init(servletConfig);

    servletConfig.getServletContext().setAttribute("reader", new WebXMLReader(servletConfig));
    servletConfig.getServletContext().setAttribute("scanner", new DefaultJaxrsScanner());
    ReaderConfigUtils.initReaderConfig(servletConfig);
  }
}
