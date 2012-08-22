package com.wordnik.swagger.jsonschema

import com.wordnik.swagger.core.DocumentationObject

abstract class JsonSchemaProvider {
  def read(hostClass: Class[_]): DocumentationObject
}