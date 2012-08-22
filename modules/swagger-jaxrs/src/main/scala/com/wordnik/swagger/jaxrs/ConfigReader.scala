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

package com.wordnik.swagger.jaxrs

import com.wordnik.swagger.core._

import javax.servlet.ServletConfig

class ConfigReader(val sc: ServletConfig) {
  def getBasePath(): String = {
    if (sc != null) sc.getInitParameter("swagger.api.basepath") else null
  }

  def getSwaggerVersion(): String = {
    SwaggerSpec.version
  }

  def getApiVersion(): String = {
    if (sc != null) sc.getInitParameter("api.version") else null
  }

  def getModelPackages(): String = {
    sc match {
      case s: ServletConfig => sc.getInitParameter("api.model.packages") match {
        case str: String => str
        case _ => ""
      }
      case _ => ""
    }
  }

  def getApiFilterClassName(): String = {
    if (sc != null) sc.getInitParameter("swagger.security.filter") else null
  }
}