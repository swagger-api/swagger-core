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

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@ExternalDocumentation(
        description = "External documentation description in class",
        url = "http://url.com"
)
public class ExternalDocsReference {
    @GET
    @Path("/")
    @Operation(externalDocs =
    @ExternalDocumentation(
            description = "External documentation description in @Operation",
            url = "http://url.com"
    )
    )
    @ExternalDocumentation(
            description = "External documentation description in method",
            url = "http://url.com"
    )
    public void setRequestBody(@RequestBody ExternalDocsSchema schema) {
    }

    @Schema(externalDocs = @ExternalDocumentation(
            description = "External documentation description in schema",
            url = "http://url.com"
    ))
    public class ExternalDocsSchema {
        public String foo;
    }

}
