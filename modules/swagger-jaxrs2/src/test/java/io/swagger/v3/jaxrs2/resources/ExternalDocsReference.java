package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


public class ExternalDocsReference {
    @GET
    @Path("/")
    @Operation(externalDocs =
        @ExternalDocumentation(
            description = "External documentation description",
            url = "http://url.com"
        )
    )
    public void setRequestBody() {
    }

}
