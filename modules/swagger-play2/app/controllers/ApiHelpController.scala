/**
 * Copyright 2012 Wordnik, Inc.
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
import play.api.{Play, Logger}
import play.modules.swagger.ApiListingCache

import javax.xml.bind.JAXBContext
import javax.xml.bind.annotation._

import java.io.StringWriter

import com.wordnik.swagger.core.util.JsonSerializer
import com.wordnik.swagger.model._
import com.wordnik.swagger.core.filter.SpecFilter
import com.wordnik.swagger.config.{ConfigFactory, FilterFactory}

import scala.collection.mutable.LinkedHashMap
import scala.reflect.runtime.{universe => ru}
import scala.reflect.runtime.universe._
import play.api.Play.current

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
  def getCode(): Int = code

  def setCode(code: Int) = this.code = code

  def getType(): String = code match {
    case ErrorResponse.ERROR => "error"
    case ErrorResponse.WARNING => "warning"
    case ErrorResponse.INFO => "info"
    case ErrorResponse.OK => "ok"
    case ErrorResponse.TOO_BUSY => "too busy"
    case _ => "unknown"
  }

  def setType(`type`: String) = {}

  def getMessage(): String = message

  def setMessage(message: String) = this.message = message
}

object ApiHelpController extends SwaggerBaseApiController {
  def getResources(filter: String = null) = Action {
    request =>
      implicit val requestHeader: RequestHeader = request

      val resourceListing = getResourceListing(if (filter == null || filter.trim.isEmpty) None else Some(filter))

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
        case None => {
          val msg = new ErrorResponse(500, "api listing for path " + path + " not found")
          Logger("swagger").error(msg.message)
          returnXml(request) match {
            case true => {
              new SimpleResult(header = ResponseHeader(500), body = play.api.libs.iteratee.Enumerator(toXmlString(msg).getBytes())).as("application/xml")
            }
            case false => {
              new SimpleResult(header = ResponseHeader(500), body = play.api.libs.iteratee.Enumerator(toJsonString(msg).getBytes())).as("application/json")
            }
          }
        }
      }
  }
}

class SwaggerBaseApiController extends Controller {
  protected def jaxbContext(): JAXBContext = JAXBContext.newInstance(classOf[String], classOf[ResourceListing])

  protected def returnXml(request: Request[_]) = request.path.contains(".xml")

  protected val AccessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")

  //Thread.currentThread().setContextClassLoader(Play.application.classloader)
  //private val mirror = ru.runtimeMirror(getClass.getClassLoader)
  private val mirror = ru.runtimeMirror(Play.application.classloader)

  trait BasePropertyHolder
  case class ModelPropertyHolder(name: String, prop: ModelProperty) extends BasePropertyHolder
  case class DynamicModelPropertyHolder(name: String, prop: DynamicModelProperty) extends BasePropertyHolder

  /**
   * Get a list of all top level resources
   */
  protected def getResourceListing(filter: Option[String])(implicit requestHeader: RequestHeader) = {
    Logger("swagger").debug("ApiHelpInventory.getRootResources")
    val docRoot = ""
    val queryParams = requestHeader.queryString.map {case (key, value) => (key -> value.toList)}
    val cookies = requestHeader.cookies.map {cookie => (cookie.name -> cookie.value)}.toMap
    val headers = requestHeader.headers.toMap.map {case (key, value) => (key -> value.toList)}

    val f = new SpecFilter
    val listings = ApiListingCache.listing(docRoot).map(specs => {
      (for (spec <- specs.values)
      yield f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)
        ).filter(m => m.apis.size > 0 && m.filter == filter)
    })
    val references = (for (listing <- listings.getOrElse(List())) yield {
      ApiListingReference(listing.resourcePath, listing.description)
    }).toList

    references.foreach {
      ref =>
        Logger("swagger").debug("reference: %s".format(ref.toString))
    }
    ResourceListing(ConfigFactory.config.getApiVersion, 
      ConfigFactory.config.getSwaggerVersion,
      references,
      List(),
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
    val queryParams = requestHeader.queryString.map {case (key, value) => (key -> value.toList)}
    val cookies = requestHeader.cookies.map {cookie => (cookie.name -> cookie.value)}.toMap
    val headers = requestHeader.headers.toMap.map {case (key, value) => (key -> value.toList)}
    val pathPart = resourceName

    val listings: List[ApiListing] = ApiListingCache.listing(docRoot).map(specs => {
      (for (spec <- specs.values) yield {
        f.filter(spec, FilterFactory.filter, queryParams, cookies, headers)
      }).filter(m => m.resourcePath == pathPart)
    }).get.toList

    listings.size match {
      case 1 => Option(buildUserListing(listings.head))
      case _ => None
    }
  }

  def buildUserListing(listing: ApiListing)(implicit requestHeader: RequestHeader) = {
    listing.copy(models = buildUserModels(listing.models))
  }

  def buildUserModels(models: Option[Map[String, Model]])(implicit requestHeader: RequestHeader) = {
    models.map(m => {
      for (
        (mk, mv) <- m
      ) yield {
        (mk, buildUserModel(mv))
      }
    })
  }

  def buildUserModel(model: Model)(implicit requestHeader: RequestHeader) = {
    if (model.dynamicProperties.nonEmpty) {
      val allProperties = (for (
        (k, v) <- model.properties
      ) yield (v.position, ModelPropertyHolder(k, v))).toList :::
        (for (
          (k, v) <- model.dynamicProperties
        ) yield (v.position, DynamicModelPropertyHolder(k, v))).toList

      val sortedProperties = allProperties.sortWith(_._1 < _._1).map(e => e._2)
      val builtProperties = buildProperties(sortedProperties, 1)

      val newProperties = new LinkedHashMap[String, ModelProperty]
      builtProperties.foreach(e => newProperties += e._1 -> e._2)

      model.copy(properties = newProperties, dynamicProperties = LinkedHashMap.empty)
    } else model
  }

  def buildProperties(props: List[BasePropertyHolder], pos: Int)(implicit requestHeader: RequestHeader): List[(String, ModelProperty)] = {
    props match {
      case (x: ModelPropertyHolder) :: xs => (x.name, x.prop.copy(position = pos)) :: buildProperties(xs, pos + 1)
      case (x: DynamicModelPropertyHolder) :: xs => {
        val properties = buildDynamicProperty(x.prop, pos)
        properties._1 ::: buildProperties(xs, properties._2 + 1)
      }
      case _ => Nil
    }
  }

  def buildDynamicProperty(prop: DynamicModelProperty, pos: Int)(implicit requestHeader: RequestHeader): (List[(String, ModelProperty)], Integer) = {
    val module = mirror.staticModule(prop.builderInstance)
    val obj = mirror.reflectModule(module)
    val instance = mirror.reflect(obj.instance)
    val sig = obj.symbol.typeSignature.member(newTermName("build"))
    val anyRes = instance.reflectMethod(sig.asMethod)(requestHeader)

    val res = try {
      anyRes.asInstanceOf[List[ModelProperty]]
    } catch {
      case e: ClassCastException => Nil
    }

    buildDynamicPropertyList(res, pos)
  }

  def buildDynamicPropertyList(props: List[ModelProperty], pos: Int): (List[(String, ModelProperty)], Integer) = {
    props match {
      case x::xs => if (x.dynamicName.isDefined) {
        val rest = buildDynamicPropertyList(xs, pos + 1)
        ((x.dynamicName.get, x.copy(position = pos, dynamicName = None)) :: rest._1, rest._2)
    } else buildDynamicPropertyList(xs, pos)
      case _ => (Nil, pos)
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
    new SimpleResult(header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(xmlValue.getBytes())).as("application/xml")
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
    new SimpleResult(header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(jsonValue.getBytes())).as("application/json")
  }
}
