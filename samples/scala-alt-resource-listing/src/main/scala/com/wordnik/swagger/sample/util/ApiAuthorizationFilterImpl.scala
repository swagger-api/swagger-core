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

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs.ApiAuthorizationFilter

import org.slf4j.LoggerFactory

import java.io.File
import java.util.ArrayList
import java.net.URLDecoder

import javax.ws.rs.core.{ HttpHeaders, UriInfo }
import javax.ws.rs.Path

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._
/**
 *
 * The rules are maintained in simple map with key as path and a boolean value indicating given path is secure or
 * not. For method level security the key is combination of http method and path .
 *
 * If the resource or method is secure then it can only be viewed using a secured api key
 *
 * Note: Objective of this class is not to provide fully functional implementation of authorization filter. This is
 * only a sample demonstration how API authorization filter works.
 *
 */

class ApiAuthorizationFilterImpl extends ApiAuthorizationFilter {
  private val logger = LoggerFactory.getLogger(classOf[ApiAuthorizationFilterImpl])

  var isFilterInitialized: Boolean = false
  var methodSecurityAnotations: Map[String, Boolean] = Map[String, Boolean]()
  var classSecurityAnotations: Map[String, Boolean] = Map[String, Boolean]()
  var securekeyId = "special-key"
  var unsecurekeyId = "default-key"

  def authorize(apiPath: String, method: String, headers: HttpHeaders, uriInfo: UriInfo): Boolean = {
    var apiKey = uriInfo.getQueryParameters.getFirst("api_key")
    val mName = method.toUpperCase;
    if (isPathSecure(mName + ":" + apiPath, false)) {
      if (apiKey == securekeyId) return true
      else return false
    }
    true
  }

  def authorizeResource(apiPath: String, headers: HttpHeaders, uriInfo: UriInfo): Boolean = {
    var apiKey = uriInfo.getQueryParameters.getFirst("api_key")
    if (isPathSecure(apiPath, true)) {
      if (apiKey == securekeyId) return true
      else return false
    } else
      true
  }

  private def isPathSecure(apiPath: String, isResource: Boolean): Boolean = {
    if (!isFilterInitialized.booleanValue()) initialize()
    if (isResource.booleanValue()) {
      if (classSecurityAnotations.contains(apiPath)) {
        classSecurityAnotations.get(apiPath).get
      } else false
    } else {
      if (methodSecurityAnotations.contains(apiPath)) {
        methodSecurityAnotations.get(apiPath).get
      } else false
    }
  }

  private def initialize() = {
    //initialize classes
    classSecurityAnotations += "/user" -> false
    classSecurityAnotations += "/pet" -> false
    classSecurityAnotations += "/store" -> true

    //initialize method security
    methodSecurityAnotations += "GET:/pet.{format}/{petId}" -> false
    methodSecurityAnotations += "POST:/pet.{format}" -> true
    methodSecurityAnotations += "PUT:/pet.{format}" -> true
    methodSecurityAnotations += "GET:/pet.{format}/findByStatus" -> false
    methodSecurityAnotations += "GET:/pet.{format}/findByTags" -> false
    methodSecurityAnotations += "GET:/store.{format}/order/{orderId}" -> true
    methodSecurityAnotations += "DELETE:/store.{format}/order/{orderId}" -> true
    methodSecurityAnotations += "POST:/store.{format}/order" -> true
    methodSecurityAnotations += "POST:/user.{format}" -> false
    methodSecurityAnotations += "PUT:/user.{format}/{username}" -> true
    methodSecurityAnotations += "DELETE:/user.{format}/{username}" -> true
    methodSecurityAnotations += "GET:/user.{format}/{username}" -> false
    methodSecurityAnotations += "GET:/user.{format}/login" -> false
    methodSecurityAnotations += "GET:/user.{format}/logout" -> false
  }
}