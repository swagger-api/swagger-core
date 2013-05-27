package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.core.SwaggerSpec
import com.wordnik.swagger.config.SwaggerConfig

import javax.servlet._

class WebXMLReader(implicit servletConfig: ServletConfig) extends SwaggerConfig {
	apiVersion = {
		servletConfig.getInitParameter("api.version") match {
			case e: String => e
			case _ => ""
		}
	}
	swaggerVersion = SwaggerSpec.version
	basePath = servletConfig.getInitParameter("swagger.api.basepath") match {
		case e: String => e
		case _ => ""
	}
	apiPath = servletConfig.getInitParameter("apiPath") match {
		case e: String => e
		case _ => ""
	}
	println("%s, %s, %s".format(apiVersion, basePath, apiPath))
}
