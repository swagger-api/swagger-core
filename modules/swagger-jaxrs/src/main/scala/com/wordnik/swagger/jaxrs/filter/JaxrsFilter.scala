package com.wordnik.swagger.jaxrs.filter

import com.wordnik.swagger.core.filter.SwaggerSpecFilter
import com.wordnik.swagger.model._

class JaxrsFilter extends SwaggerSpecFilter {
  def isOperationAllowed(operation: Operation, api: ApiDescription, params: Map[String, List[String]], cookies: Map[String, String], headers: Map[String, List[String]]): Boolean = true
  def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: Map[String, List[String]], cookies: Map[String, String], headers: Map[String, List[String]]): Boolean = true
}