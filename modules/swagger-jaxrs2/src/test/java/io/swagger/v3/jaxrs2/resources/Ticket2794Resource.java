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

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@Path("/notnullparameter")
public class Ticket2794Resource
{
    @Produces({ MediaType.APPLICATION_JSON })
    @GET
    public Response getBooks(
            @QueryParam("page") @NotNull int page) {
        return Response.ok(
                Arrays.asList(
                        new Book(),
                        new Book()
                )
        ).build();
    }

    @Path("/new_reqBody_required")
    @POST
    public Response insert(@RequestBody(required = true) Book book) {
        return Response.ok().build();
    }

    @Path("/newnotnull")
    @POST
    public Response insertnotnull(@NotNull Book book) {
        return Response.ok().build();
    }

    public static class Book {
        public String foo;
    }
}