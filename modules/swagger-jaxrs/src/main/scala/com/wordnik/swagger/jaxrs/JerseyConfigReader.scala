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

import com.sun.jersey.spi.container.servlet.WebConfig
import com.wordnik.swagger.core._
import scala.collection.JavaConverters._

class JerseyConfigReader(val wc: WebConfig) extends ConfigReader {

  def basePath(): String = findInitParam("swagger.api.basepath")

  def swaggerVersion(): String = {
    SwaggerSpec.version
  }

  def apiVersion(): String = findInitParam("api.version")

  def modelPackages(): String = {
    findInitParam("api.model.packages") match {
      case str: String => str
      case _ => ""
    }
  }

  def apiFilterClassName(): String = findInitParam("swagger.security.filter")

  /**
   * Look for an initParameter in either the ServletConfig or the ServletContext
   */
  private def findInitParam(key: String): String = {
    val scOpt = Option.apply(wc)
    scOpt.map { wc =>
      Option.apply(wc.getInitParameter(key))
        .getOrElse(wc.getServletContext.getInitParameter(key))
    } orNull
  }

}
