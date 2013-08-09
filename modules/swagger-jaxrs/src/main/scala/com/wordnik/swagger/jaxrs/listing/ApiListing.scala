package com.wordnik.swagger.jaxrs.listing

import com.wordnik.swagger.config._
import com.wordnik.swagger.reader._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.jaxrs.config._

import org.slf4j.LoggerFactory

import java.lang.annotation.Annotation
import java.lang.reflect.Method

import javax.ws.rs.core.{ UriInfo, HttpHeaders, Context, Response, MediaType, Application, MultivaluedMap }
import javax.ws.rs.core.Response._
import javax.ws.rs._
import javax.ws.rs.ext.Provider

import javax.servlet.ServletConfig

import scala.collection.mutable.LinkedHashMap

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object ApiListingCache {
  private val LOGGER = LoggerFactory.getLogger(ApiListingCache.getClass)

  var _cache: Option[Map[String, ApiListing]] = None

  def listing(docRoot: String, app: Application, sc: ServletConfig): Option[Map[String, ApiListing]] = {
    _cache.orElse{
      LOGGER.debug("loading cache")
      ClassReaders.reader.map{reader => 
        ScannerFactory.scanner.map(scanner => {
          val classes = scanner match {
            case scanner: JaxrsScanner => scanner.asInstanceOf[JaxrsScanner].classesFromContext(app, null)
            case _ => List()
          }
          // For each top level resource, parse it and look for swagger annotations.
          val listings = (for(cls <- classes) yield reader.read(docRoot, cls, ConfigFactory.config)).flatten.toList
          _cache = Some((listings.map(m => {
            // always start with "/"
            val resourcePath = m.resourcePath.startsWith ("/") match {
              case true => m.resourcePath
              case false => "/" + m.resourcePath
            }
            LOGGER.debug("adding resource path " + resourcePath)
            (resourcePath, m)
          })).toMap)
        })
      }
      _cache
    }
    if(_cache != None)
      LOGGER.debug("cache has " + _cache.get.keys + " keys")
    else
      LOGGER.debug("cache is empty")
    _cache
  }

  def invalidateCache() = {
    _cache = None
  }
}

class ApiListingResource {
  private val LOGGER = LoggerFactory.getLogger(classOf[ApiListingResource])

  @GET
  def resourceListing (
    @Context app: Application,
    @Context sc: ServletConfig,
    @Context headers: HttpHeaders,
    @Context uriInfo: UriInfo
  ): Response = {
    val docRoot = this.getClass.getAnnotation(classOf[Path]).value
    val f = new SpecFilter
    val listings = ApiListingCache.listing(docRoot, app, sc).map(specs => {
      (for(spec <- specs.values) 
        yield f.filter(spec, FilterFactory.filter, paramsToMap(uriInfo.getQueryParameters), cookiesToMap(headers), headersToMap(headers))
      ).filter(m => m.apis.size > 0)
    })
    val references = (for(listing <- listings.getOrElse(List())) yield {
      ApiListingReference(listing.resourcePath, listing.description, listing.position)
    }).toList.sortWith(_.position < _.position)

    val config = ConfigFactory.config
    val resourceListing = ResourceListing(config.apiVersion,
      config.swaggerVersion,
      references,
      config.authorizations,
      config.info
    )
    Response.ok(resourceListing).build
  }

  /**
   * individual api listing
   **/
  @GET
  @Path("/{route: .+}")
  def apiDeclaration (
    @PathParam("route") route: String,
    @Context app: Application,
    @Context sc: ServletConfig,
    @Context headers: HttpHeaders,
    @Context uriInfo: UriInfo
  ): Response = {
    LOGGER.debug("requested apiDeclaration for " + route)
    val docRoot = this.getClass.getAnnotation(classOf[Path]).value
    val f = new SpecFilter
    val pathPart = cleanRoute(route)
    LOGGER.debug("requested route " + pathPart)
    val listings = ApiListingCache.listing(docRoot, app, sc).map(specs => {
      (for(spec <- specs.values) yield {
        LOGGER.debug("inspecting path " + spec.resourcePath)
        f.filter(spec, FilterFactory.filter, paramsToMap(uriInfo.getQueryParameters), cookiesToMap(headers), headersToMap(headers))
      }).filter(m => {
        val resourcePath = m.resourcePath match {
          case e: String if(e.startsWith("/")) => e
          case e: String => "/" + e
        }
        resourcePath == pathPart
      })
    }).toList.flatten

    listings.size match {
      case 1 => Response.ok(listings(0)).build
      case _ => Response.status(404).build
    }
  }

  // ensure leading slash, remove trailing
  def cleanRoute(route: String) = {
    val cleanStart = {
      if(route.startsWith("/")) route
      else "/" + route
    }
    if(cleanStart.endsWith("/")) cleanStart.substring(0, cleanStart.length - 1)
    else cleanStart
  }

  def invalidateCache() = {
    ApiListingCache.invalidateCache()
  }

  def paramsToMap(params: MultivaluedMap[String, String]): Map[String, List[String]] = {
    (for((key, list) <- params.asScala) yield (key, list.asScala.toList)).toMap
  }

  def cookiesToMap(headers: HttpHeaders): Map[String, String] = {
    Option(headers).map(h => {
      (for((name, cookie) <- h.getCookies.asScala) yield (name, cookie.getValue)).toMap
    }).getOrElse(Map())
  }

  def headersToMap(headers: HttpHeaders): Map[String, List[String]] = {
    (for((key, values) <- headers.getRequestHeaders.asScala) yield (key, values.asScala.toList)).toMap
  }
}