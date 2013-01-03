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

package play.modules.swagger

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.JsonUtil
import com.wordnik.swagger.play._

import javax.xml.bind.JAXBContext
import java.io.StringWriter

import play.api.mvc.RequestHeader
import play.api.Play.current
import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.Predef._
import scala.collection.JavaConversions._

/**
 * Exposes two primay methods: to get a list of available resources and to get details on a given resource
 *
 * @author ayush
 * @since 10/9/11 4:05 PM
 *
 */
object ApiHelpInventory {
  Logger.debug("ApiHelpInventory.init")
  // Add the Play classloader to Swagger
  SwaggerContext.registerClassLoader(current.classloader)

  // Get a list of all controller classes
  private val controllerClasses = ListBuffer.empty[Class[_]]

  // Initialize the map from Api annotation value to play controller class
  private val resourceMap = scala.collection.mutable.Map.empty[String, Class[_]]

  // Read various configurable properties. These can be specified in application.conf
  private val apiVersion = current.configuration.getString("api.version") match { case None => "beta" case Some(value) => value }
  private val basePath = current.configuration.getString("swagger.api.basepath") match { case None => "http://localhost" case Some(value) => value }
  private val swaggerVersion = SwaggerSpec.version
  private val apiFilterClassName = current.configuration.getString("swagger.security.filter") match { case None => null case Some(value) => value }

  private val filterOutTopLevelApi = true

  def getResourceNames: java.util.List[String] = getResourceMap.keys.toList

  private val jaxbContext = JAXBContext.newInstance(classOf[Documentation]);

  /**
   * Get a list of all top level resources
   */
  private def getRootResources(format: String)(implicit requestHeader: RequestHeader) = {
    var apiFilter: ApiAuthorizationFilter = ApiAuthorizationFilterLocator.get(apiFilterClassName)

    val allApiDoc = new Documentation
    for (clazz <- getControllerClasses) {
      val apiAnnotation = clazz.getAnnotation(classOf[Api])
      if (null != apiAnnotation) {
        val listingPath = {
          if(apiAnnotation.listingPath != "") apiAnnotation.listingPath
          else apiAnnotation.value
        }.replaceAll("\\.json", PlayApiReader.formatString).replaceAll("\\.xml", PlayApiReader.formatString)
        val realPath = apiAnnotation.value.replaceAll("\\.json", PlayApiReader.formatString).replaceAll("\\.xml", PlayApiReader.formatString)

        val api = new DocumentationEndPoint(listingPath, apiAnnotation.description())
        if (!isApiAdded(allApiDoc, api)) {
          if (null == apiFilter || apiFilter.authorizeResource(realPath)) {
            allApiDoc.addApi(api)
          }
        }
      }
    }

    allApiDoc.swaggerVersion = swaggerVersion
    allApiDoc.basePath = basePath
    allApiDoc.apiVersion = apiVersion

    allApiDoc
  }

  /**
   * Get detailed API/models for a given resource
   */
  private def getResource(resourceName: String)(implicit requestHeader: RequestHeader) = {
    val qualifiedResourceName = PlayApiReader.formatString match {
      case e: String if(e != "") => {
        resourceName.replaceAll("\\.json", PlayApiReader.formatString).replaceAll("\\.xml", PlayApiReader.formatString)
      }
      case _ => resourceName
    }
    getResourceMap.get(qualifiedResourceName) match {
      case Some(cls) => {
        val apiAnnotation = cls.getAnnotation(classOf[Api])

        val listingPath = {
          if(apiAnnotation.listingPath != "") apiAnnotation.listingPath
          else apiAnnotation.value
        }.replaceAll("\\.json", PlayApiReader.formatString).replaceAll("\\.xml", PlayApiReader.formatString)
        val realPath = apiAnnotation.value.replaceAll("\\.json", PlayApiReader.formatString).replaceAll("\\.xml", PlayApiReader.formatString)

        Logger.debug("Loading resource " + qualifiedResourceName + " from " + cls + " @ " + realPath + ", " + basePath)
        val api = PlayApiReader.read(cls, apiVersion, swaggerVersion, basePath, realPath)
        val docs = new HelpApi(apiFilterClassName).filterDocs(api, realPath)
        Option(docs)
      }
      case None => {
        None
      }
    }
  }

  def getPathHelpJson(apiPath: String)(implicit requestHeader: RequestHeader): String = {
    getResource(apiPath) match {
      case Some(docs) => JsonUtil.getJsonMapper.writeValueAsString(docs)
      case None => null
    }
  }

  def getPathHelpXml(apiPath: String)(implicit requestHeader: RequestHeader): String = {
    getResource(apiPath) match {
      case Some(docs) => {
        val stringWriter = new StringWriter()
        jaxbContext.createMarshaller().marshal(docs, stringWriter);
        stringWriter.toString
      }
      case None => null
    }
  }

  def getRootHelpJson()(implicit requestHeader: RequestHeader): String = {
    JsonUtil.getJsonMapper.writeValueAsString(getRootResources("json"))
  }

  def getRootHelpXml()(implicit requestHeader: RequestHeader): String = {
    val stringWriter = new StringWriter()
    jaxbContext.createMarshaller().marshal(getRootResources("xml"), stringWriter);
    stringWriter.toString
  }

  def clear() = {
    this.controllerClasses.clear
    this.resourceMap.clear
  }

  def reload() = {
    PlayApiReader.clear
    ApiAuthorizationFilterLocator.clear

    clear()
    this.getRootResources("json")(null)
    for (resource <- this.getResourceMap.keys) {
      Logger.debug("loading resource " + resource)
      getResource(resource)(null) match {
        case Some(docs) => Logger.debug("loaded resource " + resource)
        case None => Logger.debug("load failed for resource " + resource)
      }
    }
  }

  /**
   * Get a list of all controller classes in Play
   */
  private def getControllerClasses = {
    if (this.controllerClasses.isEmpty) {
      // get from application routes
      val controllers = current.routes match {
        case Some(r) => {
          for(doc <- r.documentation) yield {
            val m1 = doc._3.lastIndexOf("(") match {
              case i:Int if (i > 0) => doc._3.substring(0, i)
              case _ => doc._3
            }
            Some(m1.substring(0, m1.lastIndexOf(".")).replace("@", ""))
          }
        }
        case None => Seq(None)
      }
      val swaggerControllers = controllers.flatten.toList
      swaggerControllers.size match {
        case i:Int if (i > 0) => {
          swaggerControllers.foreach(className => current.classloader.loadClass(className))
          swaggerControllers.foreach(clazzName => {
            val cls = current.classloader.loadClass(clazzName)
            this.controllerClasses += cls;
            val apiAnnotation = cls.getAnnotation(classOf[Api])
            if (apiAnnotation != null) {
              Logger.debug("Found Resource " + apiAnnotation.value + " @ " + clazzName)
              val path = {
                if(apiAnnotation.listingPath != "") apiAnnotation.listingPath
                else apiAnnotation.value
              }.replaceAll("\\.json", PlayApiReader.formatString).replaceAll("\\.xml", PlayApiReader.formatString)
              resourceMap += path -> cls
            } else {
              Logger.debug("class " + clazzName + " is not the right type")
            }
          })
        }
        case _ =>
      }
    }
    controllerClasses
  }

  private def getResourceMap = {
    // check if resources and controller info has already been loaded
    if (controllerClasses.length == 0)
      this.getControllerClasses
    this.resourceMap
  }

  private def isApiAdded(allApiDoc: Documentation, endpoint: DocumentationEndPoint): Boolean = {
    var isAdded: Boolean = false
    Option(allApiDoc.getApis) match {
      case Some(apis) => {
        for (addedApi <- allApiDoc.getApis()) {
          if (endpoint.path.equals(addedApi.path)) isAdded = true
        }
      }
      case None => 
    }
    isAdded
  }
}