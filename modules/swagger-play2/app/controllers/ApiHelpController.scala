/**
 *  Copyright 2012 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package controllers

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util.JsonUtil

import org.slf4j.LoggerFactory

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.Logger
import play.modules.swagger.ApiHelpInventory

import javax.xml.bind.JAXBContext
import javax.xml.bind.annotation._

import java.io.StringWriter

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._

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
  def getResources() = Action { request =>
    implicit val requestHeader: RequestHeader = request

    val resources = returnXml(request) match {
      case true => ApiHelpInventory.getRootHelpXml()
      case false => ApiHelpInventory.getRootHelpJson()
    }
    returnValue(request, resources)
  }

  def getResource(path: String) = Action { request =>
    implicit val requestHeader: RequestHeader = request

    val help = returnXml(request) match {
      case true => ApiHelpInventory.getPathHelpXml(path)
      case false => ApiHelpInventory.getPathHelpJson(path)
    }
    Option(help) match {
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
  protected def jaxbContext(): JAXBContext = JAXBContext.newInstance(classOf[String])
  protected def returnXml(request: Request[_]) = request.path.contains(".xml")
  protected val ok = "ok"
  protected val AccessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")

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
      val mapper = JsonUtil.getJsonMapper
      mapper.writeValueAsString(data)
    }
  }

  protected def JsonResponse(data: Any) = {
    val jsonValue = toJsonString(data)
    new SimpleResult(header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(jsonValue.getBytes())).as("application/json")
  }
}
