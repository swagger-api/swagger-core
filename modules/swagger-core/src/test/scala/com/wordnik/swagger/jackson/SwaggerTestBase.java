package com.wordnik.swagger.jackson;

import com.wordnik.swagger.jackson.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import junit.framework.TestCase;

public abstract class SwaggerTestBase extends TestCase {
  static ObjectMapper mapper;

  public static ObjectMapper mapper() {
    if(mapper == null) {
      mapper = new ObjectMapper();
      // mapper.registerModule(new JodaModule());
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    return mapper;
  }


  protected ModelResolver modelResolver() {
    return new ModelResolver(new ObjectMapper());
  }

  protected void prettyPrint(Object o) {
    try{
      System.out.println(mapper().writer(new DefaultPrettyPrinter()).writeValueAsString(o));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
