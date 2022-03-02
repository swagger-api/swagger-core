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

package io.swagger.v3.jaxrs2.it.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.swagger.v3.jaxrs2.it.model.Widget;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.core.Response;

@Path("/widgets")
@Tag(name="widgets")
@Produces("application/json")
@Consumes("application/json")
public class WidgetResource {

    @Path("/{widgetId}")
    @GET
    @Operation(summary = "Find pet by ID",
             description = "Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate API error conditions",
             responses = @ApiResponse(
                     content =  @Content(schema = @Schema(implementation = Widget.class)),
                     description = "Returns widget with matching id",
                     responseCode = "200"
             )
    )
    public Response getWidget(@PathParam("widgetId") String widgetId) {
        return Response.ok(new Widget().setA("foo").setB("bar")).build();
    }
}
