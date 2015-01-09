package com.wordnik.swagger.jaxrs.config;

import com.wordnik.swagger.config.Scanner;

import java.util.*;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

public class DefaultJaxrsScanner extends AbstractScanner implements JaxrsScanner {
  private boolean prettyPrint = false;

  public Set<Class<?>> classesFromContext(
    Application app,
    ServletConfig sc) {
    Set<Class<?>> output = new HashSet<Class<?>>();
    if(app != null) {
      Set<Class<?>> clz = app.getClasses();
      if(clz != null)
        output.addAll(clz);
      Set<Object> singletons = app.getSingletons();
      if(singletons != null) {
        for(Object o: singletons) {
          if(o instanceof Class)
            output.add((Class)o);
        }
      }
    }
    return output;
  }

  public Set<Class<?>>classes() {
    return new HashSet<Class<?>>();
  }

  public boolean prettyPrint() {
    return prettyPrint;
  }

  public void setPrettyPrint(boolean shouldPrettyPrint) {
    this.prettyPrint = shouldPrettyPrint;
  }
}