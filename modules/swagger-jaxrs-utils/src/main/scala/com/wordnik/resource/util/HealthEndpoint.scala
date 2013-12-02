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


import com.wordnik.swagger.core._

import com.wordnik.swagger.annotations._
import com.wordnik.util.perf.{Health, HealthSnapshot}

import javax.ws.rs.core.Response
import javax.ws.rs.{Path, GET}

trait Monitor {
  private val OK = "ok"

  @GET
  @Path("/health")
  @ApiOperation(value = "Returns health report on this JVM",
    response = classOf[Health])
  def getHealth = Response.ok.entity(HealthSnapshot.get()).build

  @GET
  @Path("/ping")
  @ApiOperation(value = "Returns ok if all's good",
    response = classOf[String])
  def ping = Response.ok.entity(OK).build
}