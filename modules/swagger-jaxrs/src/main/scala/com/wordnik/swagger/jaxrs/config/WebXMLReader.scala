package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.core.{ SwaggerSpec, SwaggerContext }
import com.wordnik.swagger.config.{ SwaggerConfig, FilterFactory }
import com.wordnik.swagger.core.filter.SwaggerSpecFilter

import org.slf4j.LoggerFactory

import javax.servlet._

class WebXMLReader(implicit servletConfig: ServletConfig) extends SwaggerConfig {
  private val LOGGER = LoggerFactory.getLogger(classOf[WebXMLReader])

  apiVersion = {
    servletConfig.getInitParameter("api.version") match {
      case e: String => {
        LOGGER.debug("set api.version to " + e); e
      }
      case _ => ""
    }
  }
  swaggerVersion = SwaggerSpec.version
  basePath = servletConfig.getInitParameter("swagger.api.basepath") match {
    case e: String => {
      LOGGER.debug("set swagger.api.basepath to " + e); e
    }
    case _ => ""
  }
  servletConfig.getInitParameter("swagger.filter") match {
    case e: String if(e != "") => {
      try {
        FilterFactory.filter = SwaggerContext.loadClass(e).newInstance.asInstanceOf[SwaggerSpecFilter]
        LOGGER.debug("set swagger.filter to " + e)
      }
      catch {
        case ex: Exception => LOGGER.error("failed to load filter " + e, ex)
      }
    }
    case _ =>
  }
}
