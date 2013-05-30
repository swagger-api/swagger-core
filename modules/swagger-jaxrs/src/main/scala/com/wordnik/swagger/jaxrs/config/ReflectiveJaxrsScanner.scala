package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.reader._
import com.wordnik.swagger.jaxrs.reader._

import org.slf4j.LoggerFactory

import org.reflections.Reflections
import org.reflections.scanners.{ SubTypesScanner, TypeAnnotationsScanner }
import org.reflections.util.{ ClasspathHelper, ConfigurationBuilder}

import javax.servlet.ServletConfig
import javax.ws.rs.core.Application

import scala.collection.JavaConverters._

class ReflectiveJaxrsScanner extends JaxrsScanner {
	private val LOGGER = LoggerFactory.getLogger(classOf[ReflectiveJaxrsScanner])
	var resourcePackage: String = _

  def getResourcePackage():String = this.resourcePackage

  def setResourcePackage(resourcePackage: String) = {
  	this.resourcePackage = resourcePackage

    // ConfigFactory.config = new WebXMLReader()
    ScannerFactory.scanner = Some(this)
    ClassReaders.reader = Some(new DefaultJaxrsApiReader)
  }

  def classesFromContext(app: Application, sc: ServletConfig) : List[Class[_]] = {
  	val config = new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(resourcePackage)).setScanners(
      new TypeAnnotationsScanner(), new SubTypesScanner())
    new Reflections(config).getTypesAnnotatedWith(classOf[Api]).asScala.toList
  }
}
