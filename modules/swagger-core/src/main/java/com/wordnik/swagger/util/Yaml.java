package com.wordnik.swagger.util;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.auth.SecuritySchemeDefinition;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class Yaml {
  static ObjectMapper mapper;
  public static ObjectMapper mapper() {
    if(mapper == null) {
      mapper = new ObjectMapper(new YAMLFactory());
      // mapper.registerModule(new JodaModule());
      SimpleModule module = new SimpleModule();
      module.addDeserializer(Model.class, new ModelDeserializer());
      module.addDeserializer(Property.class, new PropertyDeserializer());
      module.addDeserializer(Parameter.class, new ParameterDeserializer());
      module.addDeserializer(SecuritySchemeDefinition.class, new SecurityDefinitionDeserializer());
      mapper.registerModule(module);
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    return mapper;
  }

  public static ObjectWriter pretty() {
    return mapper().writer(new DefaultPrettyPrinter());
  }

  public static void prettyPrint(Object o) {
    try {
      System.out.println(pretty().writeValueAsString(o));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
