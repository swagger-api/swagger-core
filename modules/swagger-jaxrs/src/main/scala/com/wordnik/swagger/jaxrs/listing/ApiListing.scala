package com.wordnik.swagger.jaxrs.listing

import com.wordnik.swagger.core.{ Documentation, DocumentationEndPoint }
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import java.lang.annotation.Annotation

import javax.ws.rs.core.{ UriInfo, HttpHeaders, Context, Response, MediaType, Application }
import javax.ws.rs.core.Response._
import javax.ws.rs._

import javax.servlet.ServletConfig

import scala.collection.mutable.LinkedHashMap
import scala.collection.JavaConverters._

object ApiListingResource {
  var _cache: Option[LinkedHashMap[String, Class[_]]] = None

  def routes(
    app: Application,
    sc: ServletConfig,
    headers: HttpHeaders,
    uriInfo: UriInfo
  ) = {
    _cache match {
      case Some(cache) => cache
      case None => {
        val resources = app.getClasses().asScala ++ app.getSingletons().asScala.map(ref => AnnotationResolver.getClassWithAnnotation(ref.getClass, classOf[Api])).filter(_ != null)
        val cache = new LinkedHashMap[String, Class[_]]
        resources.foreach(resource => {
          val api = resource.getAnnotation(classOf[Api])
          val path = api.value.startsWith("/") match {
            case true => api.value.substring(1)
            case false => api.value
          }
          cache += path -> resource
        })
        _cache = Some(cache)
        cache
      }
    }
  }
}

class ApiListing {
  @GET
  def resourceListing(
    @Context app: Application,
    @Context sc: ServletConfig,
    @Context headers: HttpHeaders,
    @Context uriInfo: UriInfo
  ): Response = {
    val listingRoot = this.getClass.getAnnotation(classOf[Api]).value
    val reader = ConfigReaderFactory.getConfigReader(sc)
    val apiVersion = reader.apiVersion()
    val swaggerVersion = reader.swaggerVersion()
    val basePath = reader.basePath()
    val routes = ApiListingResource.routes(app, sc, headers, uriInfo)

    val apis = (for(route <- routes.map(m => m._1)) yield {
      docForRoute(route, app, sc, headers, uriInfo) match {
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
  @Path("/{route: .+}")
  def apiListing(
    @PathParam("route") route: String,
    @Context app: Application,
    @Context sc: ServletConfig,
    @Context headers: HttpHeaders,
    @Context uriInfo: UriInfo
  ): Response = {
    docForRoute(route, app, sc, headers, uriInfo) match {
      case Some(doc) => Response.ok.entity(doc).build
      case None => Response.status(Status.NOT_FOUND).build
    }
  }

  def docForRoute(
    route: String,
    app: Application,
    sc: ServletConfig,
    headers: HttpHeaders,
    uriInfo: UriInfo
  ): Option[Documentation] = {
    val reader = ConfigReaderFactory.getConfigReader(sc)
    val apiFilterClassName = reader.apiFilterClassName()
    val apiVersion = reader.apiVersion()
    val swaggerVersion = reader.swaggerVersion()
    val basePath = reader.basePath()
    val routes = ApiListingResource.routes(app, sc, headers, uriInfo)

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