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

import io.swagger.v3.jaxrs2.resources.model.NotFoundModel;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Path("/")
@Produces({ "application/xml" })
public class SimpleResourceWithVendorAnnotation {

    @VendorFunnyAnnotation
    @GET
    @Path("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = NotFoundModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "object not found")
    })
    public Response getTest(
            @PathParam("id") final String id,
            @QueryParam("limit") final Integer limit) throws WebApplicationException {
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/value")
    @Produces({ "text/plain" })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = NotFoundModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "object not found")
    })
    public Response getStringValue() throws WebApplicationException {
        return Response.ok().entity("ok").build();
    }

    /**
     * Annotation processed by some vendor libraries. It could be used by swagger because the result of that processing
     * could return with rest error response.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface VendorFunnyAnnotation {

    }
}
