package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.core.{ SwaggerContext, SwaggerSpec }
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader
import com.wordnik.swagger.config._
import com.wordnik.swagger.reader._

import org.reflections.Reflections
import org.reflections.scanners.{ SubTypesScanner, TypeAnnotationsScanner }
import org.reflections.util.{ ClasspathHelper, ConfigurationBuilder}

import org.slf4j.LoggerFactory

import javax.servlet.ServletConfig
import javax.ws.rs.core.Application

import scala.collection.JavaConverters._

class BeanConfig extends JaxrsScanner {
  private val LOGGER = LoggerFactory.getLogger(classOf[BeanConfig])
  private var resourcePackage: String = _

  ConfigFactory.config = new SwaggerConfig
  ClassReaders.reader = Some(new DefaultJaxrsApiReader)

  def getResourcePackage():String = this.resourcePackage

  def setResourcePackage(resourcePackage: String) = {
    this.resourcePackage = resourcePackage
    ScannerFactory.scanner = Some(this)
  }

  def classesFromContext(app: Application, sc: ServletConfig) : List[Class[_]] = {
    val config = new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(resourcePackage)).setScanners(
      new TypeAnnotationsScanner(), new SubTypesScanner())
    new Reflections(config).getTypesAnnotatedWith(classOf[Api]).asScala.toList
  }

  def setApiReader(reader: String) = {
    try{
      val cls = SwaggerContext.loadClass(reader).newInstance.asInstanceOf[ClassReader]
      ClassReaders.reader = Some(cls)
    }
    catch {
      case e: Exception => LOGGER.error("failed to load reader class %s".format(reader))
    }
  }

  def getApiReader(): String = ClassReaders.reader match {
    case Some(cls) => cls.getClass.getName
    case _ => ""
  }

  def setVersion(apiVersion: String) = ConfigFactory.config.apiVersion = apiVersion
  def getVersion(): String = ConfigFactory.config.apiVersion

  def setBasePath(basePath: String) = ConfigFactory.config.basePath = basePath
  def getBasePath(): String = ConfigFactory.config.basePath
}
