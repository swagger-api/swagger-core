package com.wordnik.swagger.jaxrs.config;

import com.wordnik.swagger.*;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.config.SwaggerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectiveJaxrsScanner implements SwaggerConfig {
  Logger LOGGER = LoggerFactory.getLogger(ReflectiveJaxrsScanner.class);

  public Swagger configure(Swagger swagger) {
    LOGGER.warn("not implemented");
    return swagger;
  }

}