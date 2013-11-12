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

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core._
import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.sample.data.{ PetData }
import com.wordnik.swagger.sample.model.{ Pet }
import com.wordnik.swagger.sample.exception.NotFoundException
import com.wordnik.swagger.sample.util.RestResourceUtil

import com.sun.jersey.core.header.FormDataContentDisposition
import com.sun.jersey.multipart.FormDataParam

import org.apache.commons.io.IOUtils

import java.io.{File, FileInputStream, InputStream, FileOutputStream}

import javax.servlet.http.HttpServletRequest

import javax.ws.rs.core.{ MediaType, Context, Response }
import javax.ws.rs._

@Path("/pet")
@Api(value = "/pet", description = "Operations about pets")
@Produces(Array(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML))
class PetResource extends RestResourceUtil {
  @GET
  @Path("/{petId}")
  @ApiOperation(value = "Find pet by ID", notes = "Returns a pet based on ID", response = classOf[Pet])
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID supplied"),
    new ApiResponse(code = 404, message = "Pet not found")))
  def getPetById(
    @ApiParam(value = "ID of pet that needs to be fetched", required = true, allowableValues = "range[1, 100000]")@PathParam("petId") petId: Long) = {
    PetData.getPetbyId(petId) match {
      case pet: Pet => Response.ok.entity(pet).build
      case _ => throw new NotFoundException(404, "Pet not found")
    }
  }

  @POST
  @Path("/uploadImage")
  @Consumes(Array(MediaType.MULTIPART_FORM_DATA))
  @ApiOperation(value = "uploads an image")
  def uploadFile(
    @ApiParam(value = "Additional data to pass to server") @FormDataParam("additionalMetadata") testString: String,
    @ApiParam(value = "file to upload") @FormDataParam("file") inputStream: InputStream,
    @ApiParam(value = "file detail") @FormDataParam("file") fileDetail: FormDataContentDisposition) = {
    println("testString: " + testString)
    val uploadedFileLocation = "./" + fileDetail.getFileName
    IOUtils.copy(inputStream, new FileOutputStream(uploadedFileLocation))
    val msg = "additionalMetadata: " + testString + "\nFile uploaded to " + uploadedFileLocation + ", " + (new java.io.File(uploadedFileLocation)).length + " bytes"
    val output = new com.wordnik.swagger.sample.model.ApiResponse(200, msg)
    Response.status(200).entity(output).build()
  }

  @DELETE
  @Path("/{petId}")
  @ApiOperation(value = "Deletes a pet")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid pet value")))
  def deletePet(
    @ApiParam(value = "Pet id to delete", required = true)@PathParam("petId") petId: String) = {
    PetData.deletePet(petId.toLong)
    Response.ok.build
  }

  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML))
  @ApiOperation(value = "Add a new pet to the store")
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input")))
  def addPet(
    @ApiParam(value = "Pet object that needs to be added to the store", required = true) pet: Pet) = {
    PetData.addPet(pet)
    println("added pet " + pet)
    Response.ok.entity(new com.wordnik.swagger.sample.model.ApiResponse(200, "SUCCESS")).build
  }

  @PUT
  @ApiOperation(value = "Update an existing pet")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid ID supplied"),
    new ApiResponse(code = 404, message = "Pet not found"),
    new ApiResponse(code = 405, message = "Validation exception")))
  def updatePet(
    @ApiParam(value = "Pet object that needs to be updated in the store", required = true) pet: Pet) = {
    PetData.addPet(pet)
    Response.ok.entity(new com.wordnik.swagger.sample.model.ApiResponse(200, "SUCCESS")).build
  }

  @GET
  @Path("/findByStatus")
  @ApiOperation(value = "Finds Pets by status",
    notes = "Multiple status values can be provided with comma seperated strings",
    response = classOf[Pet], responseContainer = "List")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid status value")))
  def findPetsByStatus(
    @ApiParam(value = "Status values that need to be considered for filter", required = true, defaultValue = "available",
      allowableValues = "available,pending,sold", allowMultiple = true)@QueryParam("status") status: String) = {
    if(status == null) {
      Response.ok.entity(new com.wordnik.swagger.sample.model.ApiResponse(400, "invalid status supplied")).build
    }
    else {
      var results = PetData.findPetByStatus(status)
      Response.ok(results).build
    }
  }

  @GET
  @Path("/findByTags")
  @ApiOperation(value = "Finds Pets by tags",
    notes = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
    response = classOf[Pet], responseContainer = "List")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid tag value")))
  @Deprecated
  def findPetsByTags(
    @ApiParam(value = "Tags to filter by", required = true,
      allowMultiple = true)@QueryParam("tags") tags: String) = {
    var results = PetData.findPetByTags(tags)
    Response.ok(results).build
  }

  @PATCH
  @Path("/{petId}")
  @ApiOperation(value = "partial updates to a pet",
    response = classOf[Pet],
    responseContainer = "List",
    consumes = "application/json,application/xml",
    produces = "application/json,application/xml")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid tag value")))
  def partialUpdate(
    @ApiParam(value = "ID of pet that needs to be fetched", required = true)@PathParam("petId") petId: String,
    @ApiParam(value = "Pet object that needs to be added to the store", required = true) pet: Pet) = {
    PetData.getPetbyId(getLong(0, 100000, 0, petId)) match {
      case existing: Pet => {
        existing.merge(pet)
        Response.ok.entity(PetData.getPetbyId(getLong(0, 100000, 0, petId))).build
      }
      case _ => throw new NotFoundException(404, "Pet not found")
    }
  }

  @POST
  @Path("/{petId}")
  @Consumes(Array(MediaType.APPLICATION_FORM_URLENCODED))
  @ApiOperation(value = "Updates a pet in the store with form data",
    consumes = MediaType.APPLICATION_FORM_URLENCODED)
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid input")))
  def updatePetWithForm (
   @ApiParam(value = "ID of pet that needs to be updated", required = true)@PathParam("petId") petId: String,
   @ApiParam(value = "Updated name of the pet", required = false)@FormParam("name") name: String,
   @ApiParam(value = "Updated status of the pet", required = false)@FormParam("status") status: String
   ) = {
    println((name, status))
   // PetData.addPet(pet)
    Response.ok.entity(new com.wordnik.swagger.sample.model.ApiResponse(200, "SUCCESS")).build
  }
}
