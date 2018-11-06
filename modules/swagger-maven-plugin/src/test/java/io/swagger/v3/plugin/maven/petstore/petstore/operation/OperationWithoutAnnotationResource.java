package io.swagger.v3.plugin.maven.petstore.petstore.operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
