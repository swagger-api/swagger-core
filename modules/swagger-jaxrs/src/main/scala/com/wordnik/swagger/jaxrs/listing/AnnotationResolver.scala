package com.wordnik.swagger.jaxrs.listing

import java.lang.annotation.Annotation

/**
 * Look for a particular annotation on a class and it's interfaces/superclasses
 *
 * @author jbaxter - 17/05/13
 */
object AnnotationResolver {
  def getClassWithAnnotation(clazz: Class[_], annotation: Class[_ <: Annotation]): Class[_] = {
    if (clazz.isAnnotationPresent(annotation)) {
      return clazz
    }
    for (intf <- clazz.getInterfaces) {
      if (intf.isAnnotationPresent(annotation)) {
        return intf
      }
    }
    val superClass: Class[_] = clazz.getSuperclass
    if (superClass != classOf[AnyRef] && superClass != null) {
      return getClassWithAnnotation(superClass, annotation)
    }
    null
  }
}
