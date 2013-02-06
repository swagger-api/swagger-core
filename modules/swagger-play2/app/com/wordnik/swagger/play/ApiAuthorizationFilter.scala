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

package com.wordnik.swagger.play

import play.api.mvc.RequestHeader 
import com.wordnik.swagger.core._
import play.api.Logger

trait ApiAuthorizationFilter extends AuthorizationFilter {
  def authorize(apiPath: String, httpOperation: String)(implicit requestHeader: RequestHeader): Boolean
  def authorizeResource(apiPath: String)(implicit requestHeader: RequestHeader): Boolean
}

object ApiAuthorizationFilterLocator {
  private var apiFilterLoadAttempted = false
  private var apiFilter: ApiAuthorizationFilter = null

  def clear() {
    apiFilterLoadAttempted = false
    apiFilter = null
  }

  def get(apiFilterClassName: String) = {
    if (!apiFilterLoadAttempted && null != apiFilterClassName) {
      try {
        apiFilter = SwaggerContext.loadClass(apiFilterClassName).newInstance.asInstanceOf[ApiAuthorizationFilter]
      } catch {
        case e: ClassNotFoundException => Logger.error("Unable to resolve apiFilter class " + apiFilterClassName);
        case e: ClassCastException => Logger.error("Unable to cast to apiFilter class " + apiFilterClassName);
      }
      apiFilterLoadAttempted = true
    }  
    apiFilter
  }
}