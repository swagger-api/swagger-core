package com.wordnik.swagger.config

import com.wordnik.swagger.core.SwaggerSpec

import com.wordnik.swagger.model.{ AuthorizationType, ApiInfo }

import scala.reflect.BeanProperty

class SwaggerConfig(
  @BeanProperty var apiVersion: String, 
  @BeanProperty var swaggerVersion: String, 
  @BeanProperty var basePath: String, 
  @BeanProperty var apiPath: String,
  @BeanProperty var authorizations: List[AuthorizationType] = List(),
  @BeanProperty var info: Option[ApiInfo] = None) {

  def this() = this(null, SwaggerSpec.version, null, null, List(), None)
}
