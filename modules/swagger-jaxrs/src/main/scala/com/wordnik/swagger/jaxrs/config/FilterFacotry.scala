package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.model._

object FilterFactory {
	var filter: SwaggerSpecFilter = new DefaultSpecFilter
}

class DefaultSpecFilter extends SwaggerSpecFilter {
  def isAllowed(operation: Operation): Boolean = true
  def isAllowed(parameter: Parameter, operation: Operation): Boolean = true
}