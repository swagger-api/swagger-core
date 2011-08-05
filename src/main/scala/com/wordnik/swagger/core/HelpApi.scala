/**
 *  Copyright 2011 Wordnik, Inc.
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

package com.wordnik.swagger.core

import org.slf4j.LoggerFactory
import util.TypeUtil

import org.apache.commons.lang.StringUtils

import javax.ws.rs.core.{UriInfo, HttpHeaders}
import java.rmi.server.Operation

import scala.collection._
import mutable._
import scala.collection.JavaConversions._

class HelpApi {
  private val LOGGER = LoggerFactory.getLogger(classOf[HelpApi])

  var apiFilter: ApiAuthorizationFilter = null

  def this(apiFilterClassName: String) = {
    this ()
    if (apiFilterClassName != null) {
      try {
        apiFilter = Class.forName(apiFilterClassName).newInstance.asInstanceOf[ApiAuthorizationFilter]
      }
      catch {
        case e: ClassNotFoundException => LOGGER.error("Unable to resolve apiFilter class " + apiFilterClassName);
        case e: ClassCastException => LOGGER.error("Unable to cast to apiFilter class " + apiFilterClassName);
      }
    }
  }

  def filterDocs(doc: Documentation, headers: HttpHeaders, uriInfo: UriInfo, currentApiPath: String): Documentation = {
    //todo: apply auth and filter doc to only those which apply to current request/api-key
    if (apiFilter != null) {
      var apisToRemove = new ListBuffer[DocumentationEndPoint]
      doc.getApis().foreach(
          api => {
            if (api.getOperations() != null){
              var operationsToRemove = new ListBuffer[DocumentationOperation]
              api.getOperations().foreach( apiOperation  =>
                if (!apiFilter.authorize(api.path, apiOperation.httpMethod,  headers, uriInfo)) {
                  operationsToRemove += apiOperation
                }
              )
              for(operation <- operationsToRemove)api.removeOperation(operation)
              if(null == api.getOperations() || api.getOperations().size() == 0){
                apisToRemove + api
              }
            }
         }
      );
      for (api <- apisToRemove) doc.removeApi(api)
    }
    //todo: transform path?
    loadModel(doc)
    doc
  }

  private def loadModel(d: Documentation): Unit = {
    val directTypes = getExpectedTypes(d)
    val types = TypeUtil.getReferencedClasses(directTypes)
    for (t <- types) {
      try {
        val clazz = Class.forName(t)
        val n = ApiPropertiesReader.read(clazz)
        if (null != n && null != n.getFields && n.getFields.length > 0) {
          d.addModel(n)
          d.addSchema(n.getName, n.toDocumentationSchema())
        }
      }
      catch {
        case e: ClassNotFoundException => LOGGER.error("Unable to resolve class " + t);
        case e: Exception => LOGGER.error("Unable to load model documentation for " + t, e)
      }
    }
  }

  private def getExpectedTypes(d: Documentation): List[String] = {
    val l = new HashSet[String]
    if (d.getApis() != null) {
      //	endpoints
      for (n <- d.getApis()) {
        //	operations
        for (o <- n.getOperations()) {
          //return types
          if (StringUtils.isNotBlank(o.getResponseTypeInternal())) l += o.getResponseTypeInternal().replaceAll("\\[\\]", "")
          //operation parameters -- for a POST we might have complex types
          for (r <- JavaConversions.asIterator((o.getParameters()).iterator())) {
            //	parameter types
            if (StringUtils.isNotBlank(r.getValueTypeInternal())) l += r.getValueTypeInternal().replaceAll("\\[\\]", "")
          }
        }
      }
    }
    l.toList
  }

}