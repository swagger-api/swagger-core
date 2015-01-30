package com.wordnik.swagger.jaxrs.ext;

import com.wordnik.swagger.jaxrs.DefaultParameterExtension;

import java.util.ServiceLoader;
import java.util.*;

public class SwaggerExtensions {
  private static List<SwaggerExtension> extensions = null;

  static {
    extensions = new ArrayList<SwaggerExtension>();
    ServiceLoader<SwaggerExtension> loader = ServiceLoader.load(SwaggerExtension.class);
    Iterator<SwaggerExtension> itr = loader.iterator();
    while(itr.hasNext()) {
      extensions.add(itr.next());
    }
    extensions.add(new DefaultParameterExtension());
  }

  public static List<SwaggerExtension> getExtensions() {
    return extensions;
  }

  public static Iterator<SwaggerExtension> chain() {
    return extensions.iterator();
  }
}