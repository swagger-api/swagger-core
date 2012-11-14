package com.wordnik.swagger.sample.resource.alt

import com.wordnik.swagger.core.{ Documentation, DocumentationEndPoint }
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import com.sun.jersey.api.core.ResourceConfig
import com.sun.jersey.spi.container.servlet.WebConfig

import java.lang.annotation.Annotation

import javax.ws.rs.core.{ UriInfo, HttpHeaders, Context, Response, MediaType }
import javax.ws.rs.core.Response._
import javax.ws.rs._

import scala.collection.mutable.HashMap
import scala.collection.JavaConverters._

object ApiListingResource {
  var _cache: Option[Map[String, Class[_]]] = None

  def routes(
    wc: WebConfig,
    rc: ResourceConfig,
    headers: HttpHeaders,
    uriInfo: UriInfo
  ) = {
    _cache match {
      case Some(cache) => cache
      case None => {
        val resources = rc.getRootResourceClasses.asScala ++ rc.getRootResourceSingletons.asScala.map(ref => ref.getClass)
        val cache = new HashMap[String, Class[_]]
        resources.foreach(resource => {
          resource.getAnnotation(classOf[Api]) match {
            case ep: Annotation => {
              val path = ep.value.startsWith("/") match {
                case true => ep.value.substring(1)
                case false => ep.value
              }
              cache += path -> resource
            }
            case _ => 
          }
        })
        _cache = Some(cache.toMap)
        cache
      }
    }
  }
}

class AlternateApiListing {
  @GET
  @Path("/")
  def resourceListing(
    @Context wc: WebConfig,
    @Context rc: ResourceConfig,
    @Context headers: HttpHeaders,
    @Context uriInfo: UriInfo
  ): Response = {
    val listingRoot = this.getClass.getAnnotation(classOf[Api]).value

    val reader = ConfigReaderFactory.getConfigReader(wc)
    val apiFilterClassName = reader.getApiFilterClassName()
    val apiVersion = reader.getApiVersion()
    val swaggerVersion = reader.getSwaggerVersion()
    val basePath = reader.getBasePath()
    val routes = ApiListingResource.routes(wc, rc, headers, uriInfo)

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
    @Context uriInfo: UriInfo
  ): Response = {
    docForRoute(route, wc, rc, headers, uriInfo) match {
      case Some(doc) => Response.ok.entity(doc).build
      case None => Response.status(Status.NOT_FOUND).build
    }
  }

  def docForRoute(
    route: String,
    wc: WebConfig,
    rc: ResourceConfig,
    headers: HttpHeaders,
    uriInfo: UriInfo
  ): Option[Documentation] = {
    val reader = ConfigReaderFactory.getConfigReader(wc)
    val apiFilterClassName = reader.getApiFilterClassName()
    val apiVersion = reader.getApiVersion()
    val swaggerVersion = reader.getSwaggerVersion()
    val basePath = reader.getBasePath()
    val routes = ApiListingResource.routes(wc, rc, headers, uriInfo)

    println("routes: " + routes)
    routes.contains(route) match {
      case true => {
        val cls = routes(route)
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