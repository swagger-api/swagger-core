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

import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public abstract class AbstractTrackingResource<T> extends JavaHelp {
@GET
	@Path("/{clientId}")
	@ApiOperation(value = "Find by ID", notes = "Returns a T", responseClass = "com.wordnik.swagger.sample.model.Pet")
	@ApiErrors(value = { @ApiError(code = 400, reason = "Invalid ID supplied"),
			@ApiError(code = 404, reason = "Pet not found") })
	public Response getPetById(
			@ApiParam(value = "ID to be used", required = true) @PathParam("clientId") String clientId)
			throws NotFoundException {
		if (null != clientId) {
			return Response.ok().entity("thank you").build();
		} else {
			throw new NotFoundException(404, "client not found");
		}
	}
}