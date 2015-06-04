/**
 *  Copyright 2015 SmartBear Software
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

package io.swagger.sample.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.swagger.sample.data.PetData;
import io.swagger.sample.exception.NotFoundException;
import io.swagger.sample.model.Pet;

@Path("/pet")
@Produces({"application/json", "application/xml"})
public class PetResource {
  static PetData petData = new PetData();
  static JavaRestResourceUtil ru = new JavaRestResourceUtil();

  @GET
  @Path("/{petId}")
  public Response getPetById(@PathParam("petId") String petId)
      throws NotFoundException {
    Pet pet = petData.getPetbyId(ru.getLong(0, 100000, 0, petId));
    if (null != pet) {
      return Response.ok().entity(pet).build();
    } else {
      throw new NotFoundException(404, "Pet not found");
    }
  }

  @POST
  public Response addPet(Pet pet) {
    petData.addPet(pet);
    return Response.ok().entity("SUCCESS").build();
  }

  @PUT
  public Response updatePet(Pet pet) {
    petData.addPet(pet);
    return Response.ok().entity("SUCCESS").build();
  }

  @GET
  @Path("/findByStatus")
  public Response findPetsByStatus(@QueryParam("status") String status) {
    return Response.ok(petData.findPetByStatus(status)).build();
  }

  @GET
  @Path("/findByTags")
  @Deprecated
  public Response findPetsByTags(@QueryParam("tags") String tags) {
    return Response.ok(petData.findPetByTags(tags)).build();
  }
}
