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

import javax.ws.rs.core.{ Response, MediaType }
import javax.ws.rs._

import java.net.URI

@Path("/pet.redirect")
@Produces(Array(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML))
class PetRedirectResource {
  @GET
  @Path("/{petId}")
  def getPetById(
    @PathParam("petId") petId: String) = {
    Response.temporaryRedirect(new URI("/pet/" + petId)).build
  }
}