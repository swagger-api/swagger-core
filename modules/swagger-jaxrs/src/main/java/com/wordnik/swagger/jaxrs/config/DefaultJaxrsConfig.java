package com.wordnik.swagger.jaxrs.config;

import javax.ws.rs.core.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class DefaultJaxrsConfig extends HttpServlet {
  @Override
  public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
    super.init(servletConfig);

    servletConfig.getServletContext().setAttribute("reader", new WebXMLReader(servletConfig));
    servletConfig.getServletContext().setAttribute("scanner", new DefaultJaxrsScanner());
    if("true".equals(servletConfig.getInitParameter("scan.all.resources"))) {
      // scan all resources, not just those annotated
    }
    else {

    }
  }
}
