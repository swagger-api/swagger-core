/**
 *  Copyright 2013 Wordnik, Inc.
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

package com.wordnik.swagger.core.filter

import com.wordnik.swagger.model._
import com.wordnik.swagger.converter.ModelInheritenceUtil

import org.slf4j.LoggerFactory

import scala.collection.mutable.{ ListBuffer, HashMap, HashSet }
import scala.collection.JavaConverters._

trait SwaggerSpecFilter {
  def isOperationAllowed(operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean
  def isParamAllowed(parameter: Parameter, operation: Operation, api: ApiDescription, params: java.util.Map[String, java.util.List[String]], cookies: java.util.Map[String, String], headers: java.util.Map[String, java.util.List[String]]): Boolean
}

class SpecFilter {
  val ComplexTypeMatcher = ".*\\[([a-zA-Z]*)\\].*".r
  private val LOGGER = LoggerFactory.getLogger(classOf[SpecFilter])

  def filter(listing: ApiListing, filter: SwaggerSpecFilter, params: Map[String, List[String]], cookies: Map[String, String], headers: Map[String, List[String]]) = {
    // these are required for java compatibility of the filter interface
    val javaParams = makeJava(params)
    val javaCookies = cookies.asJava
    val javaHeaders = makeJava(headers)
    val filteredApis = (for(api <- listing.apis) yield {
      val filteredOps = (for(op <- api.operations) yield {
        if(filter.isOperationAllowed(op, api, javaParams, javaCookies, javaHeaders)) {
          val filteredParams = (for(param <- op.parameters) yield {
            if(filter.isParamAllowed(param, op, api, javaParams, javaCookies, javaHeaders)) Some(param)
            else None
          }).flatten.toList
          Some(op.copy(parameters = filteredParams))
        }
        else None
      }).flatten.toList
      filteredOps.size match {
        case 0 => None
        case _ => Some(api.copy(operations = filteredOps))
      }
    }).flatten.toList
    val filteredModels = filterModels(listing.models, filteredApis)
    listing.copy(apis = filteredApis, models = filteredModels)
  }

  def makeJava(map: Map[String, List[String]]) = (for((key, values) <- map) yield (key, values.asJava)).toMap.asJava

  def filterModels(allModels: Option[Map[String, Model]], apis: List[ApiDescription]) = {
    val modelNames = requiredModels(allModels, apis)
    LOGGER.debug("required model names: " + modelNames)
    val existingModels = allModels.getOrElse(Map[String, Model]())

    LOGGER.debug("existing models: " + existingModels)
    val output = (for(model <- modelNames) yield {
      if(existingModels.contains(model))
        Some(model, existingModels(model))
      else None
    }).flatten.toMap
    val filtered = ModelInheritenceUtil.expand(output)
    if(output.size > 0) Some(filtered)
    else None
  }

  def requiredModels(allModels: Option[Map[String, Model]], apis: List[ApiDescription]): List[String] = {
    val modelNames = new ListBuffer[String]
    apis.foreach(api => {
      for(op <- api.operations) {
        modelNames += op.responseClass
        op.parameters.foreach(modelNames += _.dataType)
        op.responseMessages.foreach(_.responseModel.map{modelNames += _})
      }
    })
    val topLevelModels = (for(model <- modelNames) yield {
      model match {
        case ComplexTypeMatcher(basePart) => {
          if(basePart.indexOf(",") > 0) // it's a map, use the value only
            basePart.split(",")(1)
          else basePart
        }
        case _ => model
      }
    }).toList
    val properties = requiredProperties(topLevelModels, allModels.getOrElse(Map()), new HashSet[String]())
    topLevelModels ++ properties
  }

  def requiredProperties(models: List[String], allModels: Map[String, Model], inspectedTypes: HashSet[String]): List[String] = {
    (for(modelname <- models) yield {
      val modelnames = new HashSet[String]()
      if(allModels.contains(modelname) && !inspectedTypes.contains(modelname)) {
        val model = allModels(modelname)
        inspectedTypes += modelname
        model.properties.map(m => {
          m._2.items match {
            case Some(item) => modelnames += item.ref.getOrElse(item.`type`)
            case None => modelnames += m._2.`type`
          }
        })
      }
      modelnames.toList
    }).flatten.toList
  }
}