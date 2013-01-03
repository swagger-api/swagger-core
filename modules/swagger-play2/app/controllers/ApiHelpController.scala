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
import java.io.StringWriter

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._

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
    returnValue(request, help)
  }
}

class SwaggerBaseApiController extends Controller {
  protected def jaxbContext(): JAXBContext = JAXBContext.newInstance(classOf[String])
  protected def returnXml(request: Request[_]) = request.path.contains(".xml")
  protected val ok = "ok"
  protected val AccessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")

  protected def XmlResponse(o: Any) = {
    val xmlValue = {
      if (o.getClass.equals(classOf[String])) {
        o.asInstanceOf[String]
      } else {
        val stringWriter = new StringWriter()
        jaxbContext.createMarshaller().marshal(o, stringWriter)
        stringWriter.toString
      }
    }
    new SimpleResult[String](header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(xmlValue)).as("application/xml")
  }

  protected def returnValue(request: Request[_], obj: Any): Result = {
    val response = returnXml(request) match {
      case true => XmlResponse(obj)
      case false => JsonResponse(obj)
    }
    response.withHeaders(AccessControlAllowOrigin)
  }

  protected def JsonResponse(data: Any) = {
    val jsonValue: String = {
      if (data.getClass.equals(classOf[String])) {
        data.asInstanceOf[String]
      } else {
        val mapper = JsonUtil.getJsonMapper
        mapper.writeValueAsString(data)
      }
    }
    new SimpleResult[String](header = ResponseHeader(200), body = play.api.libs.iteratee.Enumerator(jsonValue)).as("application/json")
  }
}
