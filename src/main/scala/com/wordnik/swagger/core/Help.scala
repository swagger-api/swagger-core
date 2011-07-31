package com.wordnik.swagger.core

import com.sun.jersey.api.core.ResourceConfig
import scala.collection.JavaConversions._
import javax.servlet.ServletConfig
import javax.ws.rs.{Path, GET}
import javax.ws.rs.core.{UriInfo, HttpHeaders, Context, Response}

/**
  * @author ayush
  * @since 6/23/11 12:48 PM
  *
  */
trait Help {
  @GET
  @ApiOperation(value = "Returns information about API parameters",
    responseClass = "com.wordnik.swagger.core.Documentation", access = 20)
  def getHelp (@Context sc :ServletConfig, @Context rc:ResourceConfig,
               @Context headers: HttpHeaders, @Context uriInfo: UriInfo): Response = {

    val apiVersion = if (sc != null) sc.getInitParameter("swagger.api.version") else null
    val swagrVersion = if (sc != null) sc.getInitParameter("swagger.version") else null
    val basePath = if (sc != null) sc.getInitParameter("swagger.api.basepath") else null

    val filterOutTopLevelApi = true

    val apiFilterClassName = if (sc != null) sc.getInitParameter("swagger.security.filter") else null

    val currentApiEndPoint = this.getClass.getAnnotation(classOf[Api])
    val currentApiPath = if (currentApiEndPoint != null && filterOutTopLevelApi) currentApiEndPoint.value else null

    val docs = new HelpApi(apiFilterClassName).filterDocs(
      ApiReader.read(this.getClass, apiVersion, swagrVersion, basePath), headers, uriInfo, currentApiPath)
    Response.ok.entity(docs).build
  }

}

trait ApiListing {
  @GET
  @ApiOperation(value = "Returns list of all available api endpoints",
    responseClass="DocumentationEndPoint", mutiValueResponse = true )
  def getAllApis( @Context sc :ServletConfig, @Context rc:ResourceConfig,
               @Context headers: HttpHeaders, @Context uriInfo: UriInfo ) : Response = {

    val apiVersion = if (sc != null) sc.getInitParameter("swagger.api.version") else null
    val swagrVersion = if (sc != null) sc.getInitParameter("swagger.version") else null
    val basePath = if (sc != null) sc.getInitParameter("swagger.api.basepath") else null

    val apiFilterClassName = if (sc != null) sc.getInitParameter("swagger.security.filter") else null

    val resources = rc.getRootResourceClasses
    val apiListingEndpoint = this.getClass.getAnnotation(classOf[Api])
    val allApiDoc = new Documentation
    for (resource <- resources) {
      val wsPath = resource.getAnnotation(classOf[Api])
      var api = new DocumentationEndPoint(wsPath.value,"")
      if(!isApiAdded(allApiDoc, api)) {
        allApiDoc.addApi(api)
      }
    }

    allApiDoc.swaggerVersion = swagrVersion
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
