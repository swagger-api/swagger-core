package com.wordnik.swagger.core

import com.sun.jersey.api.core.ResourceConfig
import collection.JavaConversions
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

    val apiVersion = if (sc != null) sc.getInitParameter("swagr.api.version") else null
    val swagrVersion = if (sc != null) sc.getInitParameter("swagr.version") else null
    val basePath = if (sc != null) sc.getInitParameter("swagr.api.basepath") else null

    val filterOutTopLevelApi = true

    val apiFilterClassName = if (sc != null) sc.getInitParameter("swagr.security.filter") else null

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

    val apiVersion = if (sc != null) sc.getInitParameter("swagr.api.version") else null
    val swagrVersion = if (sc != null) sc.getInitParameter("swagr.version") else null
    val basePath = if (sc != null) sc.getInitParameter("swagr.api.basepath") else null

    val apiFilterClassName = if (sc != null) sc.getInitParameter("swagr.security.filter") else null

    val resources = rc.getRootResourceClasses
    val apiListingEndpoint = this.getClass.getAnnotation(classOf[Api])
    val allApiDoc = new Documentation
    for (resource <- JavaConversions.asIterator( ( resources ).iterator() )) {
      val doc = new HelpApi(apiFilterClassName).filterDocs( ApiReader.read(resource, apiVersion, swagrVersion, basePath) , headers, uriInfo, apiListingEndpoint.value)

      val apiEndpoint = resource.getAnnotation(classOf[Api])


      if( doc.getApis() != null ){
        for( api <- JavaConversions.asIterator((doc.getApis).iterator())){
          if(!isApiAdded(allApiDoc, api) && api.path.equals(apiEndpoint.value)) {
            api.setErrorResponses(null)
            api.setOperations(null)
            allApiDoc.addApi(api)
          }
        }
      }
    }

    allApiDoc.swagrVersion = swagrVersion
    allApiDoc.basePath = basePath
    allApiDoc.apiVersion = apiVersion

    Response.ok.entity(allApiDoc).build

  }

  private def isApiAdded(allApiDoc: Documentation, endpoint: DocumentationEndPoint): Boolean = {
    var isAdded: Boolean = false
    if (allApiDoc.getApis != null) {
      for (addedApi <- JavaConversions.asIterator((allApiDoc.getApis).iterator())) {
        if (endpoint.path.equals(addedApi.path)) isAdded = true
      }
    }
    isAdded
  }



}
