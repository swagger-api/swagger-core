/**
 *  Copyright 2014 Reverb Technologies, Inc.
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

package com.wordnik.swagger.sample.bean;

import com.wordnik.swagger.sample.data.PetData;
import com.wordnik.swagger.sample.model.Pet;
import com.wordnik.swagger.sample.exception.NotFoundException;
import com.wordnik.swagger.sample.resource.PetResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Component
public class PetResourceBean implements PetResource {
    static PetData petData = new PetData();
    static JavaRestResourceUtil ru = new JavaRestResourceUtil();

    @Override
    public Pet getPetById(String petId) throws NotFoundException {
        Pet pet = petData.getPetbyId(ru.getLong(0, 100000, 0, petId));
        if (null != pet) {
            return pet;
        } else {
            throw new NotFoundException(404, "Pet not found");
        }
    }

    @Override
    public Response addPet(Pet pet) {
        petData.addPet(pet);
        return Response.ok().entity("SUCCESS").build();
    }

    @Override
    public Response updatePet(Pet pet) {
        petData.addPet(pet);
        return Response.ok().entity("SUCCESS").build();
    }

    @Override
    public Response findPetsByStatus(String status) {
        return Response.ok(petData.findPetByStatus(status)).build();
    }

    @Override
    @Deprecated
    public Response findPetsByTags(String tags) {
        return Response.ok(petData.findPetByTags(tags)).build();
    }
}
