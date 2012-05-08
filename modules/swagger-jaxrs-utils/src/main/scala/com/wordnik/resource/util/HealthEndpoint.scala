package com.wordnik.resource.util
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

import javax.ws.rs.core.Response
import com.wordnik.swagger.core._
import javax.ws.rs.{Path, GET}
import com.wordnik.util.perf.HealthSnapshot

/**
  * @author ayush
  * @since 11/15/11 5:47 PM
  *
  */
trait Monitor {
  private val OK = "ok"

  @GET
  @Path("/health")
  @ApiOperation(value = "Returns health report on this JVM",
    responseClass = "com.wordnik.swagger.discovery.Health")
  def getHealth = Response.ok.entity(HealthSnapshot.get()).build

  @GET
  @Path("/ping")
  @ApiOperation(value = "Returns ok if all's good",
    responseClass = "String")
  def ping = Response.ok.entity(OK).build


}