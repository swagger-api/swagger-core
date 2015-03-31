package com.wordnik.swagger.jaxrs.listing;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.models.Swagger;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import javax.ws.rs.Produces;

import java.lang.reflect.Type;
import java.io.*;
import java.lang.annotation.Annotation;

@Provider
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "application/yaml"})
public class SwaggerSerializers implements MessageBodyWriter <Swagger> {
  Logger LOGGER = LoggerFactory.getLogger(SwaggerSerializers.class);

  static ObjectMapper yaml;
  static boolean prettyPrint = false;

  static {
    yaml = new ObjectMapper(new YAMLFactory());
    yaml.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    yaml.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    yaml.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static void setPrettyPrint(boolean shouldPrettyPrint) {
    SwaggerSerializers.prettyPrint = shouldPrettyPrint;
  }

  @Override
  public boolean isWriteable(Class type, Type genericType, Annotation[] annotations,
    MediaType mediaType) {
    return Swagger.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(Swagger data, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(Swagger data,
    Class<?> type,
    Type genericType, 
    Annotation[] annotations,
    MediaType mediaType,
    MultivaluedMap <String, Object> headers,
    OutputStream out) throws IOException {
    if(mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
      if(prettyPrint)
        out.write(Json.pretty().writeValueAsString(data).getBytes("utf-8"));
      else
        out.write(Json.mapper().writeValueAsString(data).getBytes("utf-8"));
    }
    else if (mediaType.toString().startsWith("application/yaml"))
      out.write(yaml.writeValueAsString(data).getBytes("utf-8"));
    else if(mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
      headers.remove("Content-Type");
      headers.add("Content-Type", "application/json");
      out.write(Json.mapper().writeValueAsString(data).getBytes("utf-8"));
    }
  }
}

