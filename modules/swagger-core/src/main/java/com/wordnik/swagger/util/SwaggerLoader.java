package com.wordnik.swagger.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.models.Swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

@Deprecated
public class SwaggerLoader {
  Logger LOGGER = LoggerFactory.getLogger(SwaggerLoader.class);

  public Swagger read(String location) {
    if(location == null)
      return null;
    System.out.println("reading from " + location);

    try {
      ObjectMapper mapper = location.toLowerCase().endsWith(".yaml") ?
        Yaml.mapper() :
        Json.mapper();

      return location.toLowerCase().startsWith("http") ?
        mapper.readValue(new URL(location), Swagger.class) :
        mapper.readValue(new File(location), Swagger.class);
    }
    catch (Exception e) {
      LOGGER.error("unable to read location " + location, e);
      return null;
    }
  }
}