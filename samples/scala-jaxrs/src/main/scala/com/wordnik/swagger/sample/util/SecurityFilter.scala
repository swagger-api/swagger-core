package com.wordnik.swagger.sample.util

import com.wordnik.swagger.model._
import com.wordnik.swagger.core.filter.SwaggerSpecFilter

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet

class SecurityFilter extends SwaggerSpecFilter {
  def isOperationAllowed(operation: Operation, api: ApiDescription, params: Map[String, List[String]], cookies: Map[String, String], headers: Map[String, List[String]]): Boolean = {
  	val isAuthorized = checkKey(params, headers)
  	if((operation.httpMethod != "GET" && !isAuthorized) || api.path.indexOf("/store") != -1) false
  	else true
  }

  def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: Map[String, List[String]], cookies: Map[String, String], headers: Map[String, List[String]]): Boolean = {
  	val isAuthorized = checkKey(params, headers)	
  	if(parameter.paramAccess == Some("internal") && !isAuthorized) false
  	else true
  }

  def checkKey(params: Map[String, List[String]], headers: Map[String, List[String]]): Boolean = {
  	val apiKey = params.contains("api_key") match {
			case true => Some(params("api_key")(0))
  		case _ => {
  			headers.contains("api_key") match {
  				case true => Some(headers("api_key")(0))
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