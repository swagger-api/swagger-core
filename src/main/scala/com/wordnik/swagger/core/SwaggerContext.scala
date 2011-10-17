package com.wordnik.swagger.core

import collection.mutable.ListBuffer
import org.slf4j.{LoggerFactory, Logger}

/**
  * @author ayush
  * @since 10/9/11 5:36 PM
  *
  */
object SwaggerContext {
  private val LOGGER = LoggerFactory.getLogger("com.wordnik.swagger.core.SwaggerContext")

  var suffixResponseFormat = true

  private val classLoaders = ListBuffer.empty[ClassLoader]
  registerClassLoader(this.getClass.getClassLoader)

  def registerClassLoader(cl: ClassLoader) = this.classLoaders += cl

  def loadClass(name: String) = {
    var clazz: Class[_] = null

    for (classLoader <- classLoaders.reverse) {
      if(clazz == null) {
        try {
          clazz = Class.forName(name, true, classLoader)
        } catch {
          case e: ClassNotFoundException => LOGGER.debug("Class not found in classLoader " + classLoader)
        }
      }
    }

    if(clazz == null)
      throw new ClassNotFoundException("class " + name + " not found")

    clazz
  }
}