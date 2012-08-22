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
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.RestResourceUtil
import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.sample.data.{ PetData }
import com.wordnik.swagger.sample.model.{ Pet }
import com.wordnik.swagger.sample.exception.NotFoundException

import com.sun.jersey.spi.resource.Singleton

import javax.ws.rs._
import javax.ws.rs.core.Response

trait PetResource extends RestResourceUtil {
  @GET
  @ApiOperation(value = "Find pet by ID", notes = "Returns a pet when ID < 10. " +
    "ID > 10 or nonintegers will simulate API error conditions", responseClass = "com.wordnik.swagger.sample.model.Pet")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID supplied"),
    new ApiError(code = 404, reason = "Pet not found")))
  def getTest(
    @ApiParam(value = "ID of pet that needs to be fetched", required = true, allowableValues = "range[0,10]")@QueryParam("petId") petId: String) = {
    var pet = PetData.getPetbyId(getLong(0, 100000, 0, petId))
    if (null != pet) {
      Response.ok.entity(pet).build
    } else {
      throw new NotFoundException(404, "Pet not found")
    }
  }

  @GET
  @Path("/{petId}")
  @ApiOperation(value = "Find pet by ID", notes = "Returns a pet when ID < 10. " +
    "ID > 10 or nonintegers will simulate API error conditions", responseClass = "com.wordnik.swagger.sample.model.Pet")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID supplied"),
    new ApiError(code = 404, reason = "Pet not found")))
  def getPetById(
    @ApiParam(value = "ID of pet that needs to be fetched", required = true, allowableValues = "range[0,10]")@PathParam("petId") petId: String) = {
    var pet = PetData.getPetbyId(getLong(0, 100000, 0, petId))
    if (null != pet) {
      Response.ok.entity(pet).build
    } else {
      throw new NotFoundException(404, "Pet not found")
    }
  }

  @POST
  @ApiOperation(value = "Add a new pet to the store")
  @ApiErrors(Array(
    new ApiError(code = 405, reason = "Invalid input")))
  def addPet(
    @ApiParam(value = "Pet object that needs to be added to the store", required = true) pet: Pet) = {
    PetData.addPet(pet)
    Response.ok.entity("SUCCESS").build
  }

  @PUT
  @ApiOperation(value = "Update an existing pet")
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID supplied"),
    new ApiError(code = 404, reason = "Pet not found"),
    new ApiError(code = 405, reason = "Validation exception")))
  def updatePet(
    @ApiParam(value = "Pet object that needs to be added to the store", required = true) pet: Pet) = {
    PetData.addPet(pet)
    Response.ok.entity("SUCCESS").build
  }

  @GET
  @Path("/findByStatus")
  @ApiOperation(value = "Finds Pets by status",
    notes = "Multiple status values can be provided with comma seperated strings",
    responseClass = "com.wordnik.swagger.sample.model.Pet", multiValueResponse = true)
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid status value")))
  def findPetsByStatus(
    @ApiParam(value = "Status values that need to be considered for filter", required = true, defaultValue = "available",
      allowableValues = "available,pending,sold", allowMultiple = true)@QueryParam("status") status: String) = {
    var results = PetData.findPetByStatus(status)
    Response.ok(results).build
  }

  @GET
  @Path("/findByTags")
  @ApiOperation(value = "Finds Pets by tags",
    notes = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
    responseClass = "com.wordnik.swagger.sample.model.Pet", multiValueResponse = true)
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid tag value")))
  @Deprecated
  def findPetsByTags(
    @ApiParam(value = "Tags to filter by", required = true,
      allowMultiple = true)@QueryParam("tags") tags: String) = {
    var results = PetData.findPetByTags(tags)
    Response.ok(results).build
  }
}

@Path("/resources/pet")
@Api(value = "/pet",
  description = "Operations about pets",
  listingPath = "/resources/pet",
  listingClass = "com.wordnik.swagger.sample.resource.PetResourceJSONXML")
@Singleton
@Produces(Array("application/json", "application/xml"))
class PetResourceListingJSON extends Help

@Path("/pet")
@Api(value = "/pet",
  description = "Operations about pets",
  listingPath = "/resources/pet")
@Singleton
@Produces(Array("application/json", "application/xml"))
class PetResourceJSONXML extends PetResource