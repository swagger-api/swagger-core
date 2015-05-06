package com.wordnik.swagger.jersey.config;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.config.ReaderConfigUtils;
import com.wordnik.swagger.jaxrs.config.WebXMLReader;

public class JerseyJaxrsConfig extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
    super.init(servletConfig);

    servletConfig.getServletContext().setAttribute("reader", new WebXMLReader(servletConfig));
    servletConfig.getServletContext().setAttribute("scanner", new DefaultJaxrsScanner());
    ReaderConfigUtils.initReaderConfig(servletConfig);
  }
}
