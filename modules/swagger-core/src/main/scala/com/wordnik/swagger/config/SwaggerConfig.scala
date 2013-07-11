package com.wordnik.swagger.config

import com.wordnik.swagger.core.SwaggerSpec

import com.wordnik.swagger.model.{ AuthorizationType, ApiInfo }

class SwaggerConfig(
  var apiVersion: String, 
  var swaggerVersion: String, 
  var basePath: String, 
  var apiPath: String,
  var authorizations: List[AuthorizationType] = List(),
  var info: Option[ApiInfo] = None) {

  def this() = this(null, SwaggerSpec.version, null, null, List(), None)
}