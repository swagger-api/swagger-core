package com.wordnik.swagger.jaxrs.config

import javax.servlet.ServletConfig

import javax.ws.rs.core.Application
import scala.collection.JavaConverters._

trait JaxrsScanner extends Scanner {
  def classes(): List[Class[_]] = List()
  def classesFromContext(app: Application, sc: ServletConfig): List[Class[_]]
}

class DefaultJaxrsScanner extends JaxrsScanner {
  def classesFromContext(app: Application, sc: ServletConfig) : List[Class[_]] = {
    if(app != null)
      (app.getClasses().asScala ++ app.getSingletons().asScala.map(ref => ref.getClass)).toList
    else List()
  }
}