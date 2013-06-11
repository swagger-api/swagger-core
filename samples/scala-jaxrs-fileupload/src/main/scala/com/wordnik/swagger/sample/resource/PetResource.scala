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
import com.wordnik.swagger.sample.util.RestResourceUtil
import com.wordnik.swagger.sample.data.{ PetData }
import com.wordnik.swagger.sample.model.{ Pet }
import com.wordnik.swagger.sample.exception.NotFoundException

import com.sun.jersey.core.header.FormDataContentDisposition
import com.sun.jersey.multipart.FormDataParam

import org.apache.commons.io.IOUtils

import java.io.{File, FileInputStream, InputStream, FileOutputStream}

import javax.servlet.http.HttpServletRequest

import javax.ws.rs.core.{ MediaType, Context, Response }
import javax.ws.rs._


@Path("/pet")
@Api(value = "/pet", description = "Operations about pets")
@Produces(Array("application/json"))
class PetResource extends RestResourceUtil {

  @POST
  @Path("/formData")
  @Consumes(Array(MediaType.MULTIPART_FORM_DATA))
  @ApiOperation(value = "uploads a form data", 
    consumes = "x-www-form-urlencoded", 
    produces = "application/json")
  def postFormData(
    @ApiParam(value = "user name") @FormDataParam("username") name: String,
    @ApiParam(value = "user age") @FormDataParam("age") age: String) = {

    val output = "thanks " + name + ", you're " + age + " years old"
    Response.status(200).entity(new com.wordnik.swagger.sample.model.ApiResponse(200, output)).build()
  }

  // to keep swagger-ui happy, and support both html form and ajax FormData POST methods, we create
  // another method which accepts application/x-www-form-urlencoded params, and calls the /formData
  // method, which accepts multipart/form-data content.  Note that @ApiOperation is not in this method
  // or it'd show as a duplicate in swagger-ui
  @POST
  @Path("/formData")
  @Consumes(Array(MediaType.APPLICATION_FORM_URLENCODED))
  def formData2(
    @FormParam("username") name: String,
    @FormParam("age") age: String) = formData(name, age)

  @GET
  @Path("/{petId}")
  @ApiOperation(value = "Find pet by ID", 
    notes = "Returns a pet based on ID", 
    response = classOf[Pet])
  @ApiErrors(Array(
    new ApiError(code = 400, reason = "Invalid ID supplied"),
    new ApiError(code = 404, reason = "Pet not found")))
  def getPetById(
    @ApiParam(value = "ID of pet that needs to be fetched", required = true)@PathParam("petId") petId: String) = {
    PetData.getPetbyId(getLong(0, 100000, 0, petId)) match {
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
    @ApiParam(value = "Pet object that needs to be updated in the store", required = true) pet: Pet) = {
    PetData.addPet(pet)
    Response.ok.entity("SUCCESS").build
  }

  @GET
  @Path("/findByStatus")
  @ApiOperation(value = "Finds Pets by status",
    notes = "Multiple status values can be provided with comma seperated strings",
    response = classOf[Pet],
    responseContainer = "List")
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
    response = classOf[Pet],
    responseContainer = "List")
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

