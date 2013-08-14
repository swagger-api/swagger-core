package com.wordnik.swagger.servlet.listing

import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.config._
import com.wordnik.swagger.model._
import com.wordnik.swagger.reader._
import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException

import javax.servlet.ServletException
import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }

class ApiDeclarationServlet extends HttpServlet {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    val route = request.getPathInfo
    if(route != null && route != "")
      renderApiDeclaration(request, response)
    else
      renderResourceListing(request, response)
  }

  def renderResourceListing(request: HttpServletRequest, response: HttpServletResponse) = {
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
    response.getOutputStream.write(JsonSerializer.asJson(resourceListing).getBytes("utf-8"))
  }

  def renderApiDeclaration(request: HttpServletRequest, response: HttpServletResponse) = {
    val route = request.getPathInfo
    val docRoot = request.getPathInfo
    val f = new SpecFilter
    val queryParams = Map[String, List[String]]()
    val cookies = Map[String, String]()
    val headers = Map[String, List[String]]()
    val pathPart = docRoot
    val listings = ApiListingCache.listing(docRoot).map(specs => {
      (for(spec <- specs.values) yield {
        f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)
      }).filter(m => m.resourcePath == pathPart)
    }).toList
    listings.size match {
      case 1 => response.getOutputStream.write(JsonSerializer.asJson(listings.head).getBytes("utf-8"))
      case _ => response.setStatus(404)
    }
  }
}