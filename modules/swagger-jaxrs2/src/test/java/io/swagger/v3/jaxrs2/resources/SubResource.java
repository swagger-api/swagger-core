/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class SubResource {
    @GET
    @Operation(description = "gets an object by ID",
            tags = "Employees")
    @ApiResponse(description = "200", responseCode = "200",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pet.class))))
    public void getAllEmployees() {
        return;
    }

    @Operation(description = "gets an object by ID",
            tags = "Employees")
    @ApiResponse(description = "200", responseCode = "200",
            content = @Content(schema = @Schema(implementation = Pet.class)))
    @GET
    @Path("{id}")
    public Pet getSubresourceOperation(@PathParam("id") Long userId) {
        return null;
    }
}