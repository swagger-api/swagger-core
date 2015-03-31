package com.wordnik.swagger.jaxrs.ext;

import com.wordnik.swagger.jaxrs.DefaultParameterExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;
import java.util.*;

public class SwaggerExtensions {
  static Logger LOGGER = LoggerFactory.getLogger(SwaggerExtensions.class);

  private static List<SwaggerExtension> extensions = null;

  static {
    extensions = new ArrayList<SwaggerExtension>();
    ServiceLoader<SwaggerExtension> loader = ServiceLoader.load(SwaggerExtension.class);
    Iterator<SwaggerExtension> itr = loader.iterator();
    while(itr.hasNext()) {
      SwaggerExtension ext = itr.next();
      LOGGER.debug("adding extension " + ext);
      extensions.add(ext);
    }
    extensions.add(new DefaultParameterExtension());
  }

  public static void setExtensions(List<SwaggerExtension> ext) {
    extensions = ext;
  }

  public static List<SwaggerExtension> getExtensions() {
    return extensions;
  }

  public static Iterator<SwaggerExtension> chain() {
    return extensions.iterator();
  }
}