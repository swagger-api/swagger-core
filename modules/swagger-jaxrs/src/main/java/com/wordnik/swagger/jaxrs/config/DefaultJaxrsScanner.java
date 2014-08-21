package com.wordnik.swagger.jaxrs.config;

import java.util.*;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

// trait JaxrsScanner extends Scanner {
//   def classes(): List[Class[_]] = List()
//   def classesFromContext(app: Application, sc: ServletConfig): List[Class[_]]
// }

public class DefaultJaxrsScanner /*extends JaxrsScanner */{
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
}