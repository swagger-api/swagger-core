/**
 *  Copyright 2015 Reverb Technologies, Inc.
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
import com.wordnik.swagger.sample.model.Owner;
import com.wordnik.swagger.sample.exception.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Api(hidden = true, value = "/owner", description = "Operations about the owners", position = 1)
@Produces("application/json")
public class OwnerResource {
	private String petId;

	public OwnerResource(){}
	public OwnerResource(String petId){
		this.petId = petId;
	}

  @GET
  @Path("/")
  @Produces("application/json")
  @ApiOperation(
    value = "Gets the owner of a pet", 
    response = Owner.class)
  public Owner getOwner() throws NotFoundException {
    Owner o = new Owner();
    o.setName("Tony");
    o.setId(1);
    return o;
  }
}
