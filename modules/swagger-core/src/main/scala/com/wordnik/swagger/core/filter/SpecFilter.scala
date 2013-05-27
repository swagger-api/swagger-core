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

import scala.collection.mutable.{ ListBuffer, HashMap, HashSet }

trait SwaggerSpecFilter {
  def isAllowed(operation: Operation): Boolean
  def isAllowed(parameter: Parameter, operation: Operation): Boolean
}

class SpecFilter {
  def filter(listing: ApiListing, filter: SwaggerSpecFilter) = {
    val filteredApis = (for(api <- listing.apis) yield {
      val filteredOps = (for(op <- api.operations) yield {
        if(filter.isAllowed(op)) {
          val filteredParams = (for(param <- op.parameters) yield {
            if(filter.isAllowed(param, op)) Some(param)
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

  def filterModels(allModels: Option[Map[String, Model]], apis: List[ApiDescription]) = {
    val modelNames = requiredModels(allModels, apis)
    val existingModels = allModels.getOrElse(Map[String, Model]())
    val output = (for(model <- modelNames) yield {
      if(existingModels.contains(model))
        Some(model, existingModels(model))
      else None
    }).flatten.toMap
    if(output.size > 0) Some(output)
    else None
  }

  val ComplexTypeMatcher = ".*\\[([a-zA-Z]*)\\].*".r

  def requiredModels(allModels: Option[Map[String, Model]], apis: List[ApiDescription]): List[String] = {
    val modelNames = new ListBuffer[String]
    apis.foreach(api => {
      for(op <- api.operations) {
        modelNames += op.responseClass
        op.parameters.foreach(modelNames += _.dataType)
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