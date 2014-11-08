package com.wordnik.swagger.jaxrs.config;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.config.Scanner;
// import com.wordnik.swagger.config.{ SwaggerConfig, FilterFactory }
// import com.wordnik.swagger.core.filter.SwaggerSpecFilter

import org.slf4j.LoggerFactory;

import javax.servlet.*;

public class WebXMLReader /*implements SwaggerConfig*/ {
  String basePath, host, filterClass, apiVersion, title, scheme = "http";

  public WebXMLReader(ServletConfig servletConfig) {
    apiVersion = servletConfig.getInitParameter("api.version");
    if(apiVersion == null) apiVersion = "Swagger Server";

    basePath = servletConfig.getInitParameter("swagger.api.basepath");
    title = servletConfig.getInitParameter("swagger.api.title");
    if(title == null)
      title = "";

    if(basePath != null) {
      String[] parts = basePath.split("://");
      if(parts.length > 1) {
        int pos = parts[1].indexOf("/");
        if(pos >= 0) {
          scheme = parts[0];
          basePath = parts[1].substring(pos);
          host = parts[1].substring(0, pos);
        }
        else {
          scheme = parts[0];
          basePath = null;
          host = parts[1];
        }
      }
    }

    filterClass = servletConfig.getInitParameter("swagger.filter");
  }

  public void read(Swagger swagger) {
    if(swagger != null) {
      Info info = swagger.getInfo();
      if(info == null)
        swagger.info(new Info());

      swagger.basePath(basePath)
        .host(host)
        .scheme(Scheme.forValue(scheme))
        .getInfo()
          .title(title)
          .version(apiVersion);
    }
  }
}
