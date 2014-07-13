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

package com.wordnik.swagger.core.util

import com.wordnik.swagger.model._

trait ReaderUtil {
  def groupByResourcePath(listings: List[com.wordnik.swagger.model.ApiListing]): List[com.wordnik.swagger.model.ApiListing] = {
    val tuples = listings.map(m => (m.resourcePath, m))
    val grouped = tuples.groupBy(_._1)
    (for (group <- grouped) yield {
      val apiDescriptions = (for(g <- group._2; api <- g._2.apis) yield api).toList
      val models = (for(g <- group._2; models <- g._2.models) yield models).flatten.toMap
      group._2(0)._2.copy(apis = apiDescriptions, models = Option(models))
    }).toList
  }
}