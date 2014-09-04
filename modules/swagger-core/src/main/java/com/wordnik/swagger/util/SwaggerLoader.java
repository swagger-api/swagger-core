package com.wordnik.swagger.util;

import com.wordnik.swagger.models.*;

import java.io.File;
import java.net.URL;

public class SwaggerLoader {
  public Swagger read(String location) {
    if(location == null)
      return null;

    System.out.println("reading from " + location);
    try {
      if(location.startsWith("http")) {
        return (Swagger) Json.mapper()
          .readValue(new URL(location), Swagger.class);
      }
      else {
        return (Swagger) Json.mapper()
          .readValue(new File(location), Swagger.class);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}