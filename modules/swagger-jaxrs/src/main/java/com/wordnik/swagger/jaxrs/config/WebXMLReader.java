package com.wordnik.swagger.jaxrs.config;

import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.Info;
// import com.wordnik.swagger.config.{ SwaggerConfig, FilterFactory }
// import com.wordnik.swagger.core.filter.SwaggerSpecFilter

import org.slf4j.LoggerFactory;

import javax.servlet.*;

public class WebXMLReader /*implements SwaggerConfig*/ {
  String basePath;
  String filterClass;
  String apiVersion;
  // private val LOGGER = LoggerFactory.getLogger(classOf[WebXMLReader])

  public WebXMLReader(ServletConfig servletConfig) {
    apiVersion = servletConfig.getInitParameter("api.version");
    if(apiVersion == null) apiVersion = "";

    basePath = servletConfig.getInitParameter("swagger.api.basepath");
    filterClass = servletConfig.getInitParameter("swagger.filter");
  }

  public void read(Swagger swagger) {
    if(swagger != null) {
      Info info = swagger.getInfo();
      if(info == null)
        swagger.info(new Info());

      swagger.basePath(basePath)
        .getInfo()
          .version(apiVersion);
    }
  }
}
