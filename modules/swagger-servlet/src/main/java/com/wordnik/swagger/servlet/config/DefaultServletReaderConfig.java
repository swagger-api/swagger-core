package com.wordnik.swagger.servlet.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.wordnik.swagger.jaxrs.config.BeanConfig;

public class DefaultServletReaderConfig extends HttpServlet {
  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);

    BeanConfig bc = new BeanConfig();
    bc.setResourcePackage(servletConfig.getInitParameter("swagger.resource.package"));
    bc.setScan(true);

    System.out.println("BEEEEEEP\007");
  }
}
