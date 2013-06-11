package com.wordnik.swagger.servlet.config

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.core.SwaggerContext
import com.wordnik.swagger.config._
import com.wordnik.swagger.reader._

import org.reflections.Reflections
import org.reflections.scanners.{ SubTypesScanner, TypeAnnotationsScanner }
import org.reflections.util.{ ClasspathHelper, ConfigurationBuilder}

import scala.collection.JavaConverters._

class ServletScanner extends Scanner {
  private var resourcePackage: String = _

  def getResourcePackage():String = this.resourcePackage

  def setResourcePackage(resourcePackage: String) = {
    this.resourcePackage = resourcePackage
  }

  def classes(): List[Class[_]] = {
    val config = new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(resourcePackage)).setScanners(
      new TypeAnnotationsScanner(), new SubTypesScanner())
    new Reflections(config).getTypesAnnotatedWith(classOf[Api]).asScala.toList
  }
}