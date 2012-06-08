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

package com.wordnik.test.swagger.core.testdata

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

class BasicResource {
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    responseClass = "com.wordnik.test.swagger.core.testdata.SampleOutput")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID"),
    new ApiError(code = 404, reason = "object not found")))
  def getTest(
    @ApiParam(value = "sample param data", required = true, allowableValues = "range[0,10]")@QueryParam("id") id: String) = {
    val out = new SampleOutput
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }

  @POST
  @ApiOperation(value = "Post object",
    notes = "No details provided",
    responseClass = "com.wordnik.test.swagger.core.testdata.SampleOutput")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID"),
    new ApiError(code = 404, reason = "object not found")))
  def postTest(
               @ApiParam(value = "sample input data", required = true)input: SampleInput) = {
    val out = new SampleOutput
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }

  @GET
  @Path("/getStringList")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    responseClass = "String",
    multiValueResponse =  true)
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID"),
    new ApiError(code = 404, reason = "object not found")))
  def getStringList() = {
    val out = new java.util.ArrayList[String]()
    out.add("foo")
    out.add("bar")
    Response.ok.entity(out).build
  }


}
