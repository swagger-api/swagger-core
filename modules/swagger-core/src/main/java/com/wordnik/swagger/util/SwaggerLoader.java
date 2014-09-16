package com.wordnik.swagger.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.models.Swagger;

import java.io.File;
import java.net.URL;

public class SwaggerLoader {
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
      e.printStackTrace();
      return null;
    }
  }
}