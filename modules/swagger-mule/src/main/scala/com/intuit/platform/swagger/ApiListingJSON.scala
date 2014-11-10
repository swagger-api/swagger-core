package com.intuit.platform.swagger

import com.wordnik.swagger.core.util.ReaderUtil
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs.config.BeanConfig
import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.config._
import com.wordnik.swagger.model._
import com.wordnik.swagger.reader._
import com.wordnik.swagger.core.util.JsonSerializer

import org.slf4j.LoggerFactory

import javax.ws.rs._
import javax.ws.rs.core.{ Response, MediaType }

@Path("/api-docs")
@Produces(Array(MediaType.APPLICATION_JSON))
class ApiListingJSON {
  private val LOGGER = LoggerFactory.getLogger(classOf[ApiListingJSON])

  @GET
  def getResource(): Response = {
    val docRoot = ""
    val queryParams = Map[String, List[String]]()
    val cookies = Map[String, String]()
    val headers = Map[String, List[String]]()

    val f = new SpecFilter
    val listings = ApiListingCache.listing(docRoot).map(specs => {
      (for(spec <- specs.values) 
        yield f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)
      ).filter(m => m.apis.size > 0)
    })
    val references = (for(listing <- listings.getOrElse(List())) yield {
      ApiListingReference(listing.resourcePath, listing.description)
    }).toList
    val config = ConfigFactory.config
    val resourceListing = ResourceListing(config.apiVersion,
      config.swaggerVersion,
      references
    )

    Response.ok(JsonSerializer.asJson(resourceListing)).build();
  }

  /**
   * individual api listing
   **/
  @GET
  @Path("/{route: .+}")
  def apiDeclaration (
    @PathParam("route") route: String
  ): Response = {
    LOGGER.debug("requested apiDeclaration for " + route)
    val docRoot = this.getClass.getAnnotation(classOf[Path]).value
    val queryParams = Map[String, List[String]]()
    val cookies = Map[String, String]()
    val headers = Map[String, List[String]]()
    val f = new SpecFilter
    val pathPart = cleanRoute(route)
    LOGGER.debug("requested route " + pathPart)
    val listings = ApiListingCache.listing(docRoot).map(specs => {
      (for(spec <- specs.values) yield {
        LOGGER.debug("inspecting path " + spec.resourcePath)
        f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)
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
}

object ApiListingCache extends ReaderUtil {
  var cache: Option[Map[String, com.wordnik.swagger.model.ApiListing]] = None

  def listing(docRoot: String): Option[Map[String, com.wordnik.swagger.model.ApiListing]] = {
    cache.orElse{
      ClassReaders.reader.map{reader => 
        ScannerFactory.scanner.map(scanner => {
          val classes = scanner match {
            case scanner: Scanner => {
              val beanConfig = scanner.asInstanceOf[BeanConfig]
              beanConfig.classesFromContext(null, null)
            }
            case _ =>List()
          }
          val listings = (for(cls <- classes) yield reader.read(docRoot, cls, ConfigFactory.config)).flatten
          val mergedListings = groupByResourcePath(listings)
          cache = Some(mergedListings.map(m => (m.resourcePath, m)).toMap)
        })
      }
      cache
    }
  }
}
