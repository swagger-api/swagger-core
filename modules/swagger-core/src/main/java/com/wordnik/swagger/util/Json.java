package com.wordnik.swagger.util;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class Json {
  static ObjectMapper mapper;
  public static ObjectMapper mapper() {
    if(mapper == null) {
      mapper = new ObjectMapper();
      mapper.registerModule(new JodaModule());
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    return mapper;
  }

  public static ObjectWriter pretty() {
    return mapper().writer(new DefaultPrettyPrinter());
  }

  public static void printPretty(Object o) {
    try {
      System.out.println(pretty().writeValueAsString(o));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}