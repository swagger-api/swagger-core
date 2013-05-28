package com.wordnik.swagger.jaxrs.config

import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.model._

object FilterFactory {
  var filter: SwaggerSpecFilter = new DefaultSpecFilter
}

class DefaultSpecFilter extends SwaggerSpecFilter {
  def isOperationAllowed(operation: Operation, api: ApiDescription, params: Map[String, List[String]], cookies: Map[String, String], headers: Map[String, List[String]]): Boolean = true
  def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: Map[String, List[String]], cookies: Map[String, String], headers: Map[String, List[String]]): Boolean = true
}