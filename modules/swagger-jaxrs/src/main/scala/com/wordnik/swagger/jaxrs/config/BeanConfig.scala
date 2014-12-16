package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.core.{ SwaggerContext, SwaggerSpec }
import com.wordnik.swagger.model.ApiInfo
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader
import com.wordnik.swagger.config._
import com.wordnik.swagger.reader._
import com.wordnik.swagger.core.filter._
import org.reflections.Reflections
import org.reflections.scanners.{ SubTypesScanner, TypeAnnotationsScanner }
import org.reflections.util.{ ClasspathHelper, ConfigurationBuilder}
import org.slf4j.LoggerFactory
import javax.servlet.ServletConfig
import javax.ws.rs.core.Application
import scala.collection.JavaConverters._
import scala.beans.BeanProperty
import javax.ws.rs.Path

class BeanConfig extends JaxrsScanner {
  private val LOGGER = LoggerFactory.getLogger(classOf[BeanConfig])
  
  ConfigFactory.config = new SwaggerConfig
  ClassReaders.reader = Some(new DefaultJaxrsApiReader)

  @BeanProperty var resourcePackage: String = _
  @BeanProperty var title: String = _
  @BeanProperty var description: String = _ 
  @BeanProperty var termsOfServiceUrl: String = _ 
  @BeanProperty var contact: String = _ 
  @BeanProperty var license: String = _ 
  @BeanProperty var licenseUrl: String = _
  @BeanProperty var filterClass: String = _

  def setScan(shouldScan: Boolean) = {
    if(title != null || description != null || termsOfServiceUrl != null || contact != null || license != null || licenseUrl != null) {
      ConfigFactory.config.info = Some(ApiInfo(title, description, termsOfServiceUrl, contact, license, licenseUrl))
    }
    if(filterClass != null) {
      try {
        FilterFactory.setFilter(SwaggerContext.loadClass(filterClass).newInstance.asInstanceOf[SwaggerSpecFilter])
      }
      catch {
        case e: Exception => {
          LOGGER.error("failed to load filter", e)
        }
      }
    }
    ScannerFactory.scanner = Some(this)
  }

  def getScan() = true

  def classesFromContext(app: Application, sc: ServletConfig) : List[Class[_]] = {
    val config = new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(resourcePackage)).setScanners(
      new TypeAnnotationsScanner(), new SubTypesScanner())
    var relections = new Reflections(config)
    (relections.getTypesAnnotatedWith(classOf[Api]).asScala ++ 
      relections.getTypesAnnotatedWith(classOf[Path]).asScala ).toList
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
