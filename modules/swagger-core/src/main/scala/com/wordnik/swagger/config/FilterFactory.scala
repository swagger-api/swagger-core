package com.wordnik.swagger.config

import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.model._

object FilterFactory {
  var filter: SwaggerSpecFilter = new DefaultSpecFilter

  def setFilter(filter: SwaggerSpecFilter) = {
    FilterFactory.filter = filter
  }

  def getFilter() = {
    filter
  }
}

class DefaultSpecFilter extends SwaggerSpecFilter {
  def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
  def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = true
}