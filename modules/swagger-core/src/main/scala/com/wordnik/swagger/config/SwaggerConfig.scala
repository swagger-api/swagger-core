package com.wordnik.swagger.config

class SwaggerConfig(
  var apiVersion: String, 
  var swaggerVersion: String, 
  var basePath: String, 
  var apiPath: String) {

  def this() = this(null, null, null, null)
}

