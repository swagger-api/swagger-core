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

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class DuplicatedOperationMethodNameResource {

    @GET
    @Path("/1")
    @Operation(operationId = "getSummaryAndDescription2",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response getSummaryAndDescription1() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/2")
    public Response getSummaryAndDescription2() {
        return Response.ok().entity("ok").build();
    }

    @POST
    @Path("/2")
    @Operation(operationId = "postSummaryAndDescription3",
            summary = "Operation Summary",
            description = "Operation Description")
    public Response postSummaryAndDescription2() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/3")
    public Response getSummaryAndDescription3() {
        return Response.ok().entity("ok").build();
    }

    @POST
    @Path("/3")
    public Response postSummaryAndDescription3() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/4")
    public Response getSummaryAndDescription3(String foo) {
        return Response.ok().entity("ok").build();
    }

}
