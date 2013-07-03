package com.wordnik.swagger.config

import com.wordnik.swagger.core.SwaggerSpec

import com.wordnik.swagger.model.AuthorizationType

class SwaggerConfig(
  var apiVersion: String, 
  var swaggerVersion: String, 
  var basePath: String, 
  var apiPath: String,
  var authorizations: List[AuthorizationType] = List()) {

  def this() = this(null, SwaggerSpec.version, null, null, null)
}