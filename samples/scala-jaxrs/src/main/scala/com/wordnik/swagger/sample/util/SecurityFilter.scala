package com.wordnik.swagger.sample.util

import com.wordnik.swagger.model._
import com.wordnik.swagger.core.filter.SwaggerSpecFilter

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet

class SecurityFilter extends SwaggerSpecFilter {
  def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = {
    checkKey(params, headers) match {
      case true => true
      case false => {
        if(operation.method == "GET" && api.path.indexOf("/store") == -1) true
        else false
      }
    }
  }

  def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean = {
    val isAuthorized = checkKey(params, headers)  
    if(parameter.paramAccess == Some("internal") && !isAuthorized) false
    else true
  }

  def checkKey(params: java.util.Map[String, java.util.List[String]], headers: java.util.Map[String, java.util.List[String]]): Boolean = {
    val apiKey = params.containsKey("api_key") match {
      case true => Some(params.get("api_key").get(0))
      case _ => {
        headers.containsKey("api_key") match {
          case true => Some(headers.get("api_key").get(0))
          case _ => None
        }
      }
    }

    apiKey match {
      case Some(key) if(key == "special-key") => true
      case _ => false
    }
  }
}