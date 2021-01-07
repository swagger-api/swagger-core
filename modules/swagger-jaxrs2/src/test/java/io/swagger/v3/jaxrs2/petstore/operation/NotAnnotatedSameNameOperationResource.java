package io.swagger.v3.jaxrs2.petstore.operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Resource With a Default Operation without Annotation
 */
public class NotAnnotatedSameNameOperationResource {
    @Path("/notannotatedoperation")
    @GET
    public String getUser() {
        return new String();
    }

    @Path("/notannotatedoperationduplicated")
    @GET
    public String getUser(final String id) {
        return new String();
    }
}
