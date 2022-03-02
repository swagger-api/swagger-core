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

import io.swagger.v3.jaxrs2.resources.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public class BasicFieldsResource {

    @GET
    @Path("/1")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription(@QueryParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            schema = @Schema(
                    description = "the generated UUID",
                    type = "string",
                    format = "uuid",
                    accessMode = Schema.AccessMode.READ_ONLY)
    ) String subscriptionId) {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/2")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription2(@QueryParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            array = @ArraySchema(maxItems = 10, minItems = 1,
                    schema = @Schema(implementation = Category.class, description = "the generated UUID"),
                    uniqueItems = true)
    ) String subscriptionId) {

        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/3")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription3(@QueryParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            schema = @Schema(
                    implementation = Category.class,
                    description = "the generated UUID",
                    accessMode = Schema.AccessMode.READ_ONLY)
    ) String subscriptionId) {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/4")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription4(@QueryParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            array = @ArraySchema(maxItems = 10, minItems = 1,
                    schema = @Schema(format = "uuid", description = "the generated UUID"),
                    uniqueItems = true)
    ) String subscriptionId) {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/5")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription5(@QueryParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true, content = @Content(mediaType = "application/json")
    ) String subscriptionId) {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/6")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription6(
            @Parameter(
                    schema = @Schema(description = "test")
            ) String subscriptionId) {

        return Response.ok().entity("ok").build();
    }
}
