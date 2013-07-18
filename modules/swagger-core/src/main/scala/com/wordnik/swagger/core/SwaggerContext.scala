package com.wordnik.swagger.core

import collection.mutable.ListBuffer
import org.slf4j.{ LoggerFactory, Logger }

object SwaggerContext {
  private val LOGGER = LoggerFactory.getLogger("com.wordnik.swagger.core.SwaggerContext")

  var suffixResponseFormat = true

  private val classLoaders = ListBuffer.empty[ClassLoader]
  registerClassLoader(this.getClass.getClassLoader)

  def registerClassLoader(cl: ClassLoader) = this.classLoaders += cl

  def loadClass(name: String) = {
    var cls: Class[_] = null
    val itr = classLoaders.reverse.iterator
    while (cls == null && itr.hasNext) {
      try {
        cls = Class.forName(name.trim, true, itr.next)
      } catch {
        case e: ClassNotFoundException => {
          LOGGER.debug("Class %s not found in classLoader".format(name))
        }
      }
    }
    if (cls == null)
      throw new ClassNotFoundException("class " + name + " not found")
    cls
  }
}