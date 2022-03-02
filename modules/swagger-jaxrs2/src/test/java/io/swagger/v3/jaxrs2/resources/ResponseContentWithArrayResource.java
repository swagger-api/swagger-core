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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

public class ResponseContentWithArrayResource {

    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    })
            }
    )
    @GET
    @SecurityRequirement(name = "JWT")
    @Path("/user")
    public List<User> getUsers() {
        return null;
    }

    class User {
        public String foo;

        @ArraySchema(arraySchema = @Schema(required = true), schema = @Schema(type = "string"))
        public List<String> issue3438;
    }
}
