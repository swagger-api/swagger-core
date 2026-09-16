package io.swagger.v3.plugins.gradle.petstore.operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Resource With a Default Operation without Annotation
 */
public class OperationWithoutAnnotationResource {
    @Path("/operationwithouannotation")
    @GET
    public String getUser() {
        return new String();
    }
}
