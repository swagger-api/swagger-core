package com.wordnik.swagger.jaxrs.ext;

import java.util.ServiceLoader;
import java.util.*;

public class SwaggerExtensions {
  private static List<SwaggerExtension> EXTENSIONS = null;

  public static List<SwaggerExtension> getExtensions() {
    if(EXTENSIONS == null) {
      EXTENSIONS = new ArrayList<SwaggerExtension>();

      ServiceLoader<SwaggerExtension> loader = ServiceLoader.load(SwaggerExtension.class);

      Iterator<SwaggerExtension> itr = loader.iterator();
      while(itr.hasNext()) {
        EXTENSIONS.add(itr.next());
      }
    }
    return EXTENSIONS;
  }
}