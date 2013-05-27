package com.wordnik.swagger.jaxrs.filter

import com.wordnik.swagger.core.filter.SwaggerSpecFilter
import com.wordnik.swagger.model._

class JaxrsFilter extends SwaggerSpecFilter {
  def isAllowed(operation: Operation) = true
  def isAllowed(parameter: Parameter, operation: Operation) = true
}