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

import java.nio.charset.Charset

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
    implicit request =>

      val resourceListing = getResourceListing

      val responseStr = if(returnXml) toXmlString(resourceListing) else toJsonString(resourceListing)
      returnValue(responseStr)
  }

  def getResource(path: String) = Action {
    implicit request =>

      getApiListing(path) match {
        case None =>
          val msg = new ErrorResponse(500, s"api listing for path $path not found")
          logger.error(msg.message)
          val responseStr = if (returnXml) toXmlString(msg) else toJsonString(msg)
          val contentType = s"application/${if (returnXml) "xml" else "json"}"
          new SimpleResult(header = ResponseHeader(500), body = Enumerator(responseStr.getBytes)).as(contentType)
        case Some(apiListing) =>
          val responseStr = if (returnXml) toXmlString(apiListing) else toJsonString(apiListing)
          returnValue(responseStr)
      }
  }
}

class SwaggerBaseApiController extends Controller {
  protected final val logger = Logger("swagger")

  protected def jaxbContext = JAXBContext.newInstance(classOf[String], classOf[ResourceListing])

  protected def returnXml(implicit request: RequestHeader) = request.path.contains(".xml")

  protected val AccessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")

  /**
   * Get a list of all top level resources
   */
  protected def getResourceListing(implicit requestHeader: RequestHeader) = {
    logger.debug("ApiHelpInventory.getRootResources")
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

    val specs = ApiListingCache.listing(docRoot = "").map(_.values).getOrElse(Nil)
    // val specs = l.getOrElse(Map: Map[String, com.wordnik.swagger.model.ApiListing] ()).map(_._2).toList
    val listings = for (spec <- specs)
    yield f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)


    val references = for (listing <- listings if listing.apis.nonEmpty) yield {
      val ref = ApiListingReference(listing.resourcePath, listing.description)
      logger.debug(s"reference: $ref")
      ref
    }

    ResourceListing(
      ConfigFactory.config.getApiVersion,
      ConfigFactory.config.getSwaggerVersion,
      references.toList,
      ConfigFactory.config.authorizations,
      ConfigFactory.config.info
    )
  }

  /**
   * Get detailed API/models for a given resource
   */
  protected def getApiListing(resourceName: String)(implicit requestHeader: RequestHeader) = {
    logger.debug(s"ApiHelpInventory.getResource($resourceName)")
    val f = new SpecFilter
    val queryParams = requestHeader.queryString.map {case (key, value) => key -> value.toList}
    val cookies = requestHeader.cookies.map {cookie => cookie.name -> cookie.value}.toMap
    val headers = requestHeader.headers.toMap.map {case (key, value) => key -> value.toList}

    val listings: Iterable[ApiListing] = ApiListingCache.listing(docRoot = "").map(specs =>
      (for (spec <- specs.values) yield {
        f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)
      }).filter(_.resourcePath == resourceName)
    ).getOrElse(Nil)

    listings.size match {
      case 1 => listings.headOption
      case _ => None
    }
  }

  def toXmlString(data: AnyRef): String = data match {
    case s: String => s
    case _ => {
      val stringWriter = new StringWriter()
      jaxbContext.createMarshaller().marshal(data, stringWriter)
      stringWriter.toString
    }
  }

  protected def XmlResponse(data: AnyRef) = {
    val xmlValue = toXmlString(data)
    new SimpleResult(header = ResponseHeader(200), body = Enumerator(xmlValue.getBytes)).as(s"application/xml")
  }

  protected def returnValue(obj: String)(implicit request: RequestHeader): Result = {
    val response = if (returnXml(request)) XmlResponse(obj) else JsonResponse(obj)
    response.withHeaders(AccessControlAllowOrigin)
  }

  def toJsonString(data: AnyRef): String = data match {
    case s: String => s
    case d: AnyRef => JsonSerializer.asJson(d)
  }

  protected def JsonResponse(data: AnyRef) = {
    val jsonValue = toJsonString(data)
    new SimpleResult(header = ResponseHeader(200), body = Enumerator(jsonValue.getBytes)).as(s"application/json")
  }
}
