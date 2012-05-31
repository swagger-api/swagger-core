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
import com.wordnik.swagger.annotations._

import org.codehaus.jackson.JsonGenerationException
import org.codehaus.jackson.map.JsonMappingException

import com.sun.jersey.api.core.ResourceConfig

import java.io.IOException
import java.lang.reflect.Constructor
import javax.servlet.ServletConfig
import javax.ws.rs.core._
import javax.ws.rs.core.Response.Status
import javax.ws.rs._

abstract class JavaHelp {
  @GET def getHelp(@Context servConfig: ServletConfig, @Context resConfig: ResourceConfig, @Context headers: HttpHeaders, @Context uriInfo: UriInfo): Response = {
    var configReader: ConfigReader = ConfigReaderFactory.getConfigReader(servConfig)
    var apiVersion: String = configReader.getApiVersion
    var swaggerVersion: String = configReader.getSwaggerVersion
    var basePath: String = configReader.getBasePath
    var apiFilterClassName: String = configReader.getApiFilterClassName
    var filterOutTopLevelApi: Boolean = true
    var currentApiEndPoint: Api = this.getClass.getAnnotation(classOf[Api])
    if (currentApiEndPoint == null) {
      return Response.status(Status.NOT_FOUND).build
    }
    else {
      var apiPath: String = null
      if (filterOutTopLevelApi) {
        apiPath = currentApiEndPoint.value
      }
      else apiPath = null
      var apiListingPath: String = null
      if (filterOutTopLevelApi) {
        if (!("" == currentApiEndPoint.listingPath)) apiListingPath = currentApiEndPoint.listingPath
        else apiListingPath = currentApiEndPoint.value
      }
      else apiListingPath = null
      var listingClass: Class[_] = this.getClass
      if (!("" == currentApiEndPoint.listingClass)) {
        listingClass = SwaggerContext.loadClass(currentApiEndPoint.listingClass)
      }
      var helpApi: HelpApi = new HelpApi(apiFilterClassName)
      var docs: Documentation = helpApi.filterDocs(JaxrsApiReader.read(listingClass, apiVersion, swaggerVersion, basePath, apiPath), headers, uriInfo, apiListingPath, apiPath)
      return Response.ok.entity(docs).build
    }
  }
}

