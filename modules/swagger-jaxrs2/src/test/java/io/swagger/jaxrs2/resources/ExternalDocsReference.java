package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.ExternalDocumentation;
import io.swagger.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by RafaelLopez on 5/21/17.
 */
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
