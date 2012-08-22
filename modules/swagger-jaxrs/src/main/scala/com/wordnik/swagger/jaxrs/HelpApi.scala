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

package com.wordnik.swagger.jaxrs

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util.TypeUtil

import org.slf4j.LoggerFactory

import org.apache.commons.lang.StringUtils

import javax.ws.rs.core.{ UriInfo, HttpHeaders }
import java.rmi.server.Operation

import scala.collection._
import mutable._
import scala.collection.JavaConversions._

class HelpApi {
  private val LOGGER = LoggerFactory.getLogger(classOf[HelpApi])

  var apiFilter: AuthorizationFilter = null

  def this(apiFilterClassName: String) = {
    this()
    if (apiFilterClassName != null) {
      try {
        apiFilter = SwaggerContext.loadClass(apiFilterClassName).newInstance.asInstanceOf[AuthorizationFilter]
      } catch {
        case e: ClassNotFoundException => LOGGER.error("Unable to resolve apiFilter class " + apiFilterClassName);
        case e: ClassCastException => LOGGER.error("Unable to cast to apiFilter class " + apiFilterClassName);
      }
    }
  }

  def filterDocs(doc: Documentation, headers: HttpHeaders, uriInfo: UriInfo, currentApiPath: String, listingPath: String): Documentation = {
    if (apiFilter != null) {
      var apisToRemove = new ListBuffer[DocumentationEndPoint]
      if (null != doc.getApis()) {
        doc.getApis().foreach(api => {
          if (api.getOperations() != null) {
            var operationsToRemove = new ListBuffer[DocumentationOperation]
            api.getOperations().foreach(apiOperation =>
              apiFilter match {
                case apiAuthFilter: ApiAuthorizationFilter => {
                  if (!apiAuthFilter.authorize(api.path, apiOperation.httpMethod, headers, uriInfo))
                    operationsToRemove += apiOperation
                }
                case filter: FineGrainedApiAuthorizationFilter => {
                  if (!filter.authorizeOperation(api.path, apiOperation, headers, uriInfo))
                    operationsToRemove += apiOperation
                }
                case _ =>
              })
            operationsToRemove.foreach(operation => api.removeOperation(operation))
            if (null == api.getOperations() || api.getOperations().size() == 0)
              apisToRemove += api
          }
        })
        apisToRemove.foreach(api => doc.removeApi(api))
      }
    }
    loadModel(doc)
    doc
  }

  private def loadModel(d: Documentation) = {
    val directTypes = getExpectedTypes(d)
    val types = TypeUtil.getReferencedClasses(directTypes)
    types.foreach(t => {
      try {
        val c = SwaggerContext.loadClass(t)
        val n = ApiPropertiesReader.read(c)
        if (null != n && null != n.getFields && n.getFields.length > 0) {
          d.addModel(n.getName, n.toDocumentationSchema())
        } else {
          if (null == n) LOGGER.error("Skipping model " + t + ". Could not load the model.")
          else if (null == n.getFields || n.getFields.length == 0)
            LOGGER.error("Skipping model " + t + ". Did not find any public fields or bean-properties in this model. If its a scala class its fields might not have @BeanProperty annotation added to its fields.")
        }
      } catch {
        case e: ClassNotFoundException => LOGGER.error("Unable to resolve class " + t);
        case e: Exception => LOGGER.error("Unable to load model documentation for " + t, e)
      }
    })
  }

  private def getExpectedTypes(d: Documentation): List[String] = {
    val l = new HashSet[String]
    if (d.getApis() != null) {
      d.getApis().foreach(n => { // endpoints
        n.getOperations().foreach(o => { //	operations
          //return types
          if (StringUtils.isNotBlank(o.getResponseTypeInternal())) l += o.getResponseTypeInternal().replaceAll("\\[\\]", "")
          //operation parameters -- for a POST we might have complex types
          if (o.getParameters() != null) {
            o.getParameters.foreach(r => {
              if (StringUtils.isNotBlank(r.getValueTypeInternal())) l += r.getValueTypeInternal().replaceAll("\\[\\]", "")
            })
          }
        })
      })
    }
    l.toList
  }
}