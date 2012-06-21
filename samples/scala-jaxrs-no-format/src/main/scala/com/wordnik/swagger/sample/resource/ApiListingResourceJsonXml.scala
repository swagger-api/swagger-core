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

package com.wordnik.swagger.sample.resource

import com.wordnik.swagger.core._
import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.annotations._

import javax.ws.rs.{Produces, Path}

@Path("/resources")
@Api("/resources")
@Produces(Array("application/json"))
class ApiListingResourceJson extends ApiListing

@Path("/resources.xml")
@Api("/resources")
@Produces(Array("application/xml"))
class ApiListingResourceXml extends ApiListing
