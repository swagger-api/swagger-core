/**
 * Copyright 2014 Reverb Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import play.api.mvc._
import play.api.Logger
import play.api.libs.iteratee.Enumerator
import play.modules.swagger.ApiListingCache

import javax.xml.bind.JAXBContext
import javax.xml.bind.annotation._

import java.io.StringWriter

import com.wordnik.swagger.core.util.JsonSerializer
import com.wordnik.swagger.model.{ApiListing, ApiListingReference, ResourceListing}
import com.wordnik.swagger.core.filter.SpecFilter
import com.wordnik.swagger.config.{ConfigFactory, FilterFactory}

object ErrorResponse {
  val ERROR = 1
  val WARNING = 2
  val INFO = 3
  val OK = 4
  val TOO_BUSY = 5
}

class ErrorResponse(@XmlElement var code: Int, @XmlElement var message: String) {
  def this() = this(0, null)

  @XmlTransient
  def getCode: Int = code

  def setCode(code: Int) = this.code = code

  def getType: String = code match {
    case ErrorResponse.ERROR => "error"
    case ErrorResponse.WARNING => "warning"
    case ErrorResponse.INFO => "info"
    case ErrorResponse.OK => "ok"
    case ErrorResponse.TOO_BUSY => "too busy"
    case _ => "unknown"
  }

  def setType(`type`: String) = {}

  def getMessage: String = message

  def setMessage(message: String) = this.message = message
}

object ApiHelpController extends SwaggerBaseApiController {

  def getResources = Action {
    request =>
      implicit val requestHeader: RequestHeader = request

      val resourceListing = getResourceListing

      val responseStr = returnXml(request) match {
        case true => toXmlString(resourceListing)
        case false => toJsonString(resourceListing)
      }
      returnValue(request, responseStr)
  }

  def getResource(path: String) = Action {
    request =>
      implicit val requestHeader: RequestHeader = request

      val apiListing = getApiListing(path)

      val responseStr = returnXml(request) match {
        case true => toXmlString(apiListing)
        case false => toJsonString(apiListing)
      }
      Option(responseStr) match {
        case Some(help) => returnValue(request, help)
        case None =>
          val msg = new ErrorResponse(500, "api listing for path " + path + " not found")
          Logger("swagger").error(msg.message)
          if (returnXml(request)) {
            InternalServerError.chunked(Enumerator(toXmlString(msg).getBytes)).as("application/xml")
          } else {
            InternalServerError.chunked(Enumerator(toJsonString(msg).getBytes)).as("application/json")
          }
      }
  }
}

class SwaggerBaseApiController extends Controller {
  protected def jaxbContext = JAXBContext.newInstance(classOf[String], classOf[ResourceListing])

  protected def returnXml(request: Request[_]) = request.path.contains(".xml")

  protected val AccessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")

  /**
   * Get a list of all top level resources
   */
  protected def getResourceListing(implicit requestHeader: RequestHeader) = {
    Logger("swagger").debug("ApiHelpInventory.getRootResources")
    val docRoot = ""
    val queryParams = (for((key, value) <- requestHeader.queryString) yield {
      (key, value.toList)
    }).toMap
    val cookies = (for(cookie <- requestHeader.cookies) yield {
      (cookie.name, cookie.value)
    }).toMap
    val headers = (for((key, value) <- requestHeader.headers.toMap) yield {
      (key, value.toList)
    }).toMap

    val f = new SpecFilter
    val l: Option[Map[String, com.wordnik.swagger.model.ApiListing]] = ApiListingCache.listing(docRoot)

    val specs: List[com.wordnik.swagger.model.ApiListing] = l match {
      case Some(m) => m.map(_._2).toList
      case _ => List()
    }
    // val specs = l.getOrElse(Map: Map[String, com.wordnik.swagger.model.ApiListing] ()).map(_._2).toList
    val listings = (for (spec <- specs)
      yield f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)
    ).filter(m => m.apis.size > 0)

    val references = (for (listing <- listings) yield {
      ApiListingReference(listing.resourcePath, listing.description)
    }).toList

    references.foreach {
      ref =>
        Logger("swagger").debug("reference: %s".format(ref.toString))
    }
    ResourceListing(
      ConfigFactory.config.getApiVersion, 
      ConfigFactory.config.getSwaggerVersion,
      references,
      ConfigFactory.config.authorizations,
      ConfigFactory.config.info
    )
  }

  /**
   * Get detailed API/models for a given resource
   */
  protected def getApiListing(resourceName: String)(implicit requestHeader: RequestHeader) = {
    Logger("swagger").debug("ApiHelpInventory.getResource(%s)".format(resourceName))
    val docRoot = ""
    val f = new SpecFilter
    val queryParams = requestHeader.queryString.map {case (key, value) => key -> value.toList}
    val cookies = requestHeader.cookies.map {cookie => cookie.name -> cookie.value}.toMap
    val headers = requestHeader.headers.toMap.map {case (key, value) => key -> value.toList}
    val pathPart = resourceName

    val listings: List[ApiListing] = ApiListingCache.listing(docRoot).map(specs => {
      (for (spec <- specs.values) yield {
        f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)
      }).filter(m => m.resourcePath == pathPart)
    }).get.toList

    listings.size match {
      case 1 => Option(listings.head)
      case _ => None
    }
  }

  def toXmlString(data: Any): String = {
    if (data.getClass.equals(classOf[String])) {
      data.asInstanceOf[String]
    } else {
      val stringWriter = new StringWriter()
      jaxbContext.createMarshaller().marshal(data, stringWriter)
      stringWriter.toString
    }
  }

  protected def XmlResponse(data: Any) = {
    val xmlValue = toXmlString(data)
    Ok.chunked(Enumerator(xmlValue.getBytes)).as("application/xml")
  }

  protected def returnValue(request: Request[_], obj: Any): Result = {
    val response = returnXml(request) match {
      case true => XmlResponse(obj)
      case false => JsonResponse(obj)
    }
    response.withHeaders(AccessControlAllowOrigin)
  }

  def toJsonString(data: Any): String = {
    if (data.getClass.equals(classOf[String])) {
      data.asInstanceOf[String]
    } else {
      JsonSerializer.asJson(data.asInstanceOf[AnyRef])
    }
  }

  protected def JsonResponse(data: Any) = {
    val jsonValue = toJsonString(data)
    Ok.chunked(Enumerator(jsonValue.getBytes)).as("application/json")
  }
}
