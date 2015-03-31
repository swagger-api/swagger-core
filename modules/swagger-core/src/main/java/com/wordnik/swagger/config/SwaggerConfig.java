package com.wordnik.swagger.config;

import com.wordnik.swagger.models.Swagger;

public interface SwaggerConfig {
  Swagger configure(Swagger swagger);  
  String getFilterClass();
}