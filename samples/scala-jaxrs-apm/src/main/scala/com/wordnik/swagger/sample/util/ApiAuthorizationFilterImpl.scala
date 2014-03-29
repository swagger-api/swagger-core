/**
 *  Copyright 2012 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.sample.util

import com.wordnik.swagger.model._
import com.wordnik.swagger.core.filter.SwaggerSpecFilter

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet

class ApiAuthorizationFilterImpl extends SwaggerSpecFilter {
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