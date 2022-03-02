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

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class SingleExampleResource {

    @POST
    @Path("/test1")
    public Response test1(
            @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"foo\" : \"foo\", \"bar\" : \"bar\"}")
                    )
            ) final User user) {
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/test2")
    @Operation(requestBody = @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class),
                    examples = @ExampleObject(value = "{\"foo\" : \"foo\", \"bar\" : \"bar\"}")
            )
    ))
    public Response test2(final User user) {
        return Response.ok().entity("").build();
    }
}
