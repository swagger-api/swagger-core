/**
 *  Copyright 2011 Wordnik, Inc.
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

package com.wordnik.swagger.core

import com.sun.jersey.api.core.ResourceConfig

import org.slf4j.LoggerFactory

import javax.servlet.ServletConfig
import javax.ws.rs.{Path, GET}
import javax.ws.rs.core.{UriInfo, HttpHeaders, Context, Response}

import scala.collection.JavaConversions._

trait Help {
  @GET
  @ApiOperation(value = "Returns information about API parameters",
    responseClass = "com.wordnik.swagger.core.Documentation")
  def getHelp (@Context sc :ServletConfig, @Context rc:ResourceConfig,
               @Context headers: HttpHeaders, @Context uriInfo: UriInfo): Response = {

    val apiVersion = if (sc != null) sc.getInitParameter("api.version") else null
    val swaggerVersion = SwaggerSpec.version
    val basePath = if (sc != null) sc.getInitParameter("swagger.api.basepath") else null

    val filterOutTopLevelApi = true

    val apiFilterClassName = if (sc != null) sc.getInitParameter("swagger.security.filter") else null

    val currentApiEndPoint = this.getClass.getAnnotation(classOf[Api])
    val currentApiPath = if (currentApiEndPoint != null && filterOutTopLevelApi) currentApiEndPoint.value else null

    val docs = new HelpApi(apiFilterClassName).filterDocs(
      ApiReader.read(this.getClass, apiVersion, swaggerVersion, basePath, currentApiPath), headers, uriInfo, currentApiPath)
    Response.ok.entity(docs).build
  }

}

trait ApiListing {

  private val LOGGER = LoggerFactory.getLogger(classOf[ApiListing])

  @GET
  @ApiOperation(value = "Returns list of all available api endpoints",
    responseClass="DocumentationEndPoint", multiValueResponse = true )
  def getAllApis( @Context sc :ServletConfig, @Context rc:ResourceConfig,
               @Context headers: HttpHeaders, @Context uriInfo: UriInfo ) : Response = {

    val apiVersion = if (sc != null) sc.getInitParameter("api.version") else null
    val swaggerVersion = SwaggerSpec.version
    val basePath = if (sc != null) sc.getInitParameter("swagger.api.basepath") else null

    val apiFilterClassName = if (sc != null) sc.getInitParameter("swagger.security.filter") else null
    var apiFilter: AuthorizationFilter = null
    if(null != apiFilterClassName) {
      try {
        apiFilter = SwaggerContext.loadClass(apiFilterClassName).newInstance.asInstanceOf[AuthorizationFilter]
      }
      catch {
        case e: ClassNotFoundException => LOGGER.error("Unable to resolve apiFilter class " + apiFilterClassName);
        case e: ClassCastException => LOGGER.error("Unable to cast to apiFilter class " + apiFilterClassName);
      }
    }

    val resources = rc.getRootResourceClasses
    val apiListingEndpoint = this.getClass.getAnnotation(classOf[Api])
    val allApiDoc = new Documentation
    for (resource <- resources) {
      val wsPath = resource.getAnnotation(classOf[Api])
      if(null != wsPath && wsPath.value != ApiReader.LIST_RESOURCES_PATH){
        var api = new DocumentationEndPoint(wsPath.value+"."+ApiReader.FORMAT_STRING, wsPath.description())
        if(!isApiAdded(allApiDoc, api)) {
          if (null != apiFilter){
            apiFilter match {
              case apiAuthFilter:ApiAuthorizationFilter => {
                if(apiAuthFilter.authorizeResource(api.path, headers, uriInfo)){
                  allApiDoc.addApi(api)
                }
              }
              case fineGrainedApiAuthFilter:FineGrainedApiAuthorizationFilter => {
                if(fineGrainedApiAuthFilter.authorizeResource(api.path, api, headers, uriInfo)){
                  allApiDoc.addApi(api)
                }
              }
              case _ => {}
            }
          }else{
            allApiDoc.addApi(api)
          }
        }
      }
    }

    allApiDoc.swaggerVersion = swaggerVersion
    allApiDoc.basePath = basePath
    allApiDoc.apiVersion = apiVersion

    Response.ok.entity(allApiDoc).build

  }

  private def isApiAdded(allApiDoc: Documentation, endpoint: DocumentationEndPoint): Boolean = {
    var isAdded: Boolean = false
    if (allApiDoc.getApis != null) {
      for (addedApi <- allApiDoc.getApis()) {
        if (endpoint.path.equals(addedApi.path)) isAdded = true
      }
    }
    isAdded
  }



}
