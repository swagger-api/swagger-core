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

package io.swagger.v3.jaxrs2.resources.ticket3624;

import io.swagger.v3.jaxrs2.resources.ticket3624.model.ByIdResponse;
import io.swagger.v3.jaxrs2.resources.ticket3624.model.ContainerizedResponse;
import io.swagger.v3.jaxrs2.resources.ticket3624.model.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Tag(name = "ExampleService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(Service.ROOT_PATH)
public interface Service {
    String ROOT_PATH = "/example";

    @GET
    @Path("/model")
    @Operation(summary = " Retrieve models for display to the user")
    Response getModels();


    @GET
    @Path("/model/by/ids")
    @Operation(summary = " Retrieve models by their ids")
    ByIdResponse getModelsById();

    @GET
    @Path("/containerized/model")
    @Operation(summary = " Retrieve review insights for a specific product")
    ContainerizedResponse getContainerizedModels();

}
