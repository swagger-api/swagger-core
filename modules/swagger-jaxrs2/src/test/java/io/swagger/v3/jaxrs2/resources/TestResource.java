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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {
    @Path("/status")
    @GET
    @Operation(description = "Get status")
    public String getStatus() {
        return "{\"status\":\"OK!\"}";
    }

    @Path("/more")
    @Operation(description = "Get more")
    @Produces({MediaType.APPLICATION_XML})
    public TestSubResource getSubResource(
            @QueryParam("qp") Integer qp) {
        return new TestSubResource();
    }

    @Path("/evenmore")
    @Operation(description = "Get even more")
    @Produces({MediaType.APPLICATION_XML})
    @Consumes(MediaType.APPLICATION_JSON)
    public TestSubResource getEvenMoreSubResource(Pet pet) {
        return new TestSubResource();
    }
}
