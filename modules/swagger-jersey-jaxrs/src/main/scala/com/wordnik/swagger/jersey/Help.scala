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

package com.wordnik.swagger.jersey

import com.sun.jersey.spi.container.servlet.WebConfig;
import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util.TypeUtil
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import org.slf4j.LoggerFactory

import javax.ws.rs.{ Path, GET }
import javax.ws.rs.core.{ UriInfo, HttpHeaders, Context, Response }
import javax.ws.rs.core.Response.Status

import scala.collection.JavaConversions._

@deprecated
trait Help {
  @GET
  @ApiOperation(value = "Returns information about API parameters",
    responseClass = "com.wordnik.swagger.core.Documentation")
  def getHelp(
    @Context wc: WebConfig, 
    @Context headers: HttpHeaders, 
    @Context uriInfo: UriInfo): Response = {
    val reader = ConfigReaderFactory.getConfigReader(wc)

    val apiVersion = reader.apiVersion
    val swaggerVersion = reader.swaggerVersion
    val basePath = reader.basePath
    val apiFilterClassName = reader.apiFilterClassName
    reader.modelPackages.split(",").foreach(p => TypeUtil.addAllowablePackage(p))

    val filterOutTopLevelApi = true
    val currentApiEndPoint = this.getClass.getAnnotation(classOf[Api])
    if (currentApiEndPoint == null) {
      Response.status(Status.NOT_FOUND).build
    } else {
      val apiPath = {
        if (filterOutTopLevelApi) {
          currentApiEndPoint.value
        } else null
      }
      val apiListingPath = {
        if (filterOutTopLevelApi) {
          if (!"".equals(currentApiEndPoint.listingPath)) currentApiEndPoint.listingPath
          else currentApiEndPoint.value
        } else null
      }
      val listingClass: Class[_] = {
        if (currentApiEndPoint.listingClass != "") {
          SwaggerContext.loadClass(currentApiEndPoint.listingClass)
        } else this.getClass
      }
      val docs = new HelpApi(apiFilterClassName).filterDocs(
        JaxrsApiReader.read(listingClass, apiVersion, swaggerVersion, basePath, apiPath),
        headers,
        uriInfo,
        apiListingPath,
        apiPath)

      docs.basePath = basePath
      docs.apiVersion = apiVersion
      docs.swaggerVersion = swaggerVersion
      Response.ok.entity(docs).build
    }
  }
}

object ConfigReaderFactory {
  def getConfigReader(wc: WebConfig): ConfigReader = {
    var configReaderStr = {
      wc.getInitParameter("swagger.config.reader") match {
        case s: String => s
        case _ => "com.wordnik.swagger.jaxrs.JerseyConfigReader"
      }
    }
    val constructor = SwaggerContext.loadClass(configReaderStr).getConstructor(classOf[WebConfig])
    val configReader = constructor.newInstance(wc).asInstanceOf[ConfigReader]
    configReader
  }
}
