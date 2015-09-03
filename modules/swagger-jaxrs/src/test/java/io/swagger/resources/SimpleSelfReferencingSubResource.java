package io.swagger.resources;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Produces({"application/xml"})
public class SimpleSelfReferencingSubResource {

    @Path("/sub")
    public SubResource getSubResource() {
        return new SubResource();
    }

    // keep this test resource simple with just a single level of self-reference
    public static class SubResource {
        @GET
        public SubResource getSelf() {
            return this;
        }
    }
}