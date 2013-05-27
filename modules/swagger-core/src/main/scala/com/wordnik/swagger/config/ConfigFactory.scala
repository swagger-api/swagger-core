package com.wordnik.swagger.config

import com.wordnik.swagger.core.SwaggerSpec

object ConfigFactory {
  var config: SwaggerConfig = new SwaggerConfig("0.0", SwaggerSpec.version, "http://localhost:8080", "")
}
