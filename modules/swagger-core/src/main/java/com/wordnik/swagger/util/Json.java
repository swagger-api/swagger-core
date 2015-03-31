package com.wordnik.swagger.util;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.auth.SecuritySchemeDefinition;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.Module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

import java.util.ServiceLoader;

public class Json {
  static Logger LOGGER = LoggerFactory.getLogger(Json.class);
  static ObjectMapper mapper;
  public static ObjectMapper mapper() {
    if(mapper == null) {
      mapper = create();
    }
    return mapper;
  }

  public static ObjectMapper create() {
    mapper = new ObjectMapper();

    // load any jackson modules 
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Property.class, new PropertyDeserializer());
    module.addDeserializer(Model.class, new ModelDeserializer());
    module.addDeserializer(Parameter.class, new ParameterDeserializer());
    module.addDeserializer(SecuritySchemeDefinition.class, new SecurityDefinitionDeserializer());
    mapper.registerModule(module);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return mapper;
  }

  public static ObjectWriter pretty() {
    return mapper().writer(new DefaultPrettyPrinter());
  }

  public static String pretty(Object o) {
    try {
      return pretty().writeValueAsString(o);
    }
    catch (Exception e) {
      return null;
    }
  }

  public static void prettyPrint(Object o) {
    try {
      System.out.println(pretty().writeValueAsString(o).replace("\r", ""));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}