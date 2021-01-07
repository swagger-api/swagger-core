package io.swagger.v3.jaxrs2.petstore.operation;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Resource With a Default Operation without Annotation
 */
public class AnnotatedSameNameOperationResource {
    @Path("/sameOperationName")
    @GET
    @Operation(description = "Same Operation Name")
    public String getUser() {
        return new String();
    }

    @Path("//sameOperationName")
    @DELETE
    @Operation(description = "Same Operation Name Duplicated")
    public String getUser(final String id) {
        return new String();
    }
}
