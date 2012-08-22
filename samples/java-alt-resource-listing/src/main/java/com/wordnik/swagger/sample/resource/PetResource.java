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

package com.wordnik.swagger.sample.resource;

import com.wordnik.swagger.annotations.*;
import com.wordnik.swagger.sample.data.PetData;
import com.wordnik.swagger.sample.model.Pet;
import com.wordnik.swagger.sample.exception.NotFoundException;
import com.wordnik.swagger.jaxrs.JavaHelp;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

public class PetResource extends JavaHelp {
	static PetData petData = new PetData();
	static JavaRestResourceUtil ru = new JavaRestResourceUtil();

	@GET
	@Path("/{petId}")
	@ApiOperation(value = "Find pet by ID", notes = "Returns a pet when ID < 10. "
			+ "ID > 10 or nonintegers will simulate API error conditions", responseClass = "com.wordnik.swagger.sample.model.Pet")
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid ID supplied"),
			@ApiError(code = 404, reason = "Pet not found") })
	public Response getPetById(
			@ApiParam(value = "ID of pet that needs to be fetched", allowableValues = "range[1,5]", required = true) @PathParam("petId") String petId)
			throws NotFoundException {
		Pet pet = petData.getPetbyId(ru.getLong(0, 100000, 0, petId));
		if (null != pet) {
			return Response.ok().entity(pet).build();
		} else {
			throw new NotFoundException(404, "Pet not found");
		}
	}

	@POST
	@ApiOperation(value = "Add a new pet to the store")
	@ApiErrors(value = { @ApiError(code = 405, reason = "Invalid input") })
	public Response addPet(
			@ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet) {
		petData.addPet(pet);
		return Response.ok().entity("SUCCESS").build();
	}

	@PUT
	@ApiOperation(value = "Update an existing pet")
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid ID supplied"),
			@ApiError(code = 404, reason = "Pet not found"),
			@ApiError(code = 405, reason = "Validation exception") })
	public Response updatePet(
			@ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet) {
		petData.addPet(pet);
		return Response.ok().entity("SUCCESS").build();
	}

	@GET
	@Path("/findByStatus")
	@ApiOperation(value = "Finds Pets by status", notes = "Multiple status values can be provided with comma seperated strings", responseClass = "com.wordnik.swagger.sample.model.Pet", multiValueResponse = true)
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid status value") })
	public Response findPetsByStatus(
			@ApiParam(value = "Status values that need to be considered for filter", required = true, defaultValue = "available", allowableValues = "available,pending,sold", allowMultiple = true) @QueryParam("status") String status) {
		return Response.ok(petData.findPetByStatus(status)).build();
	}

	@GET
	@Path("/findByTags")
	@ApiOperation(value = "Finds Pets by tags", notes = "Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.", responseClass = "com.wordnik.swagger.sample.model.Pet", multiValueResponse = true)
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid tag value") })
	@Deprecated
	public Response findPetsByTags(
			@ApiParam(value = "Tags to filter by", required = true, allowMultiple = true) @QueryParam("tags") String tags) {
		return Response.ok(petData.findPetByTags(tags)).build();
	}
}
