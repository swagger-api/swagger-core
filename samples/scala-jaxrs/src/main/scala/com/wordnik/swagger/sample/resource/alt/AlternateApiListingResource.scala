package com.wordnik.swagger.sample.resource.alt


import com.wordnik.swagger.core.{ Documentation, DocumentationEndPoint }
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import com.wordnik.swagger.sample.resource._

import com.sun.jersey.api.core.ResourceConfig
import com.sun.jersey.spi.container.servlet.WebConfig

import java.lang.annotation.Annotation

import javax.ws.rs.core.{ UriInfo, HttpHeaders, Context, Response, MediaType }
import javax.ws.rs.core.Response._
import javax.ws.rs._

import scala.collection.JavaConverters._

@Path("/api-docs.json")
@Api("/api-docs")
@Produces(Array("application/json"))
class ApiListingResourceJSON extends AlternateApiListingResource

@Path("/api-docs.xml")
@Api("/api-docs")
@Produces(Array("application/xml"))
class ApiListingResourceXML extends AlternateApiListingResource

/**
 * you add your routes explicitly
 **/
class AlternateApiListingResource {
	val routes = Map(
		"pet" -> classOf[PetResourceJSON],
		"store" -> classOf[PetStoreResourceJSON],
		"user" -> classOf[UserResourceJSON])

	@GET
	def resourceListing(
		@Context wc: WebConfig,
    @Context rc: ResourceConfig,
    @Context headers: HttpHeaders,
    @Context uriInfo: UriInfo): Response = {
		val listingRoot = this.getClass.getAnnotation(classOf[Api]).value

		val reader = ConfigReaderFactory.getConfigReader(wc)
		val apiFilterClassName = reader.getApiFilterClassName()
	  val apiVersion = reader.getApiVersion()
	  val swaggerVersion = reader.getSwaggerVersion()
	  val basePath = reader.getBasePath()

    val apis = (for(route <- routes.map(m => m._1)) yield {
    	docForRoute(route, wc, rc, headers, uriInfo) match {
    		case Some(doc) if(doc.getApis !=null && doc.getApis.size > 0) => {
    			Some(new DocumentationEndPoint(listingRoot + JaxrsApiReader.FORMAT_STRING + doc.resourcePath, ""))
    		}
    		case _ => None
    	}
    }).flatten.toList

    val doc = new Documentation()
    doc.apiVersion = apiVersion
    doc.swaggerVersion = swaggerVersion
    doc.basePath = basePath
    doc.setApis(apis.asJava)

    Response.ok.entity(doc).build
	}

	/**
	 * individual api listing
	 **/
  @GET
  @Path("/{route}")
  def apiListing(
  	@PathParam("route") route: String,
  	@Context wc: WebConfig,
    @Context rc: ResourceConfig,
    @Context headers: HttpHeaders,
    @Context uriInfo: UriInfo): Response = {

  	docForRoute(route, wc, rc, headers, uriInfo) match {
  		case Some(doc) => Response.ok.entity(doc).build
  		case None => Response.status(Status.NOT_FOUND).build
  	}
  }

  /**
   * ignore, will move to a base class
   **/
  def docForRoute(
  	route: String,
  	@Context wc: WebConfig,
    @Context rc: ResourceConfig,
    @Context headers: HttpHeaders,
    @Context uriInfo: UriInfo): Option[Documentation] = {
		val reader = ConfigReaderFactory.getConfigReader(wc)
		val apiFilterClassName = reader.getApiFilterClassName()
	  val apiVersion = reader.getApiVersion()
	  val swaggerVersion = reader.getSwaggerVersion()
	  val basePath = reader.getBasePath()

  	routes(route) match {
  		case cls: Class[_] => {
		    cls.getAnnotation(classOf[Api]) match {
		    	case currentApiEndPoint: Annotation => {
			      val apiPath = currentApiEndPoint.value
			      val apiListingPath = currentApiEndPoint.value

			      val doc = new HelpApi(apiFilterClassName).filterDocs(
			        JaxrsApiReader.read(cls, apiVersion, swaggerVersion, basePath, apiPath),
			        headers,
			        uriInfo,
			        apiListingPath,
			        apiPath)

			      doc.basePath = basePath
			      doc.apiVersion = apiVersion
			      doc.swaggerVersion = swaggerVersion
			      Some(doc)
			    }
			    case _ => None
			  }
  		}
  		case _ => None
  	}
  }
}