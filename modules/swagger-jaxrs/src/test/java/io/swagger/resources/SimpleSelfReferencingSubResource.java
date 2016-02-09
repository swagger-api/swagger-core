package io.swagger.resources;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class SimpleSelfReferencingSubResource {

    @Path("/sub")
    public SubResource retrieveSubResource() {
        return new SubResource();
    }

    public static class SubResource {

        @GET
        @Produces("application/json")
        public SubResource retrieve() {
            return this;
        }

        @Path("/recurse")
        public SubResource retrieveSelf() {
            return this;
        }
        @Path("/recurse2")
        public SubResource2 retrieveSelf2() {
            return new SubResource2();
        }
        @Path("/leaf")
        public SubResource3 retrieveLeaf() {
            return new SubResource3();
        }
    }

    public static class SubResource2 {

        @GET
        @Produces("application/json")
        public SubResource2 retrieve() {
            return this;
        }

        @Path("/recurse")
        public SubResource2 retrieveSelf() {
            return this;
        }
        @Path("/recurse1")
        public SubResource retrieveSelf1() {
            return new SubResource();
        }
        @Path("/leaf")
        public SubResource3 retrieveLeaf() {
            return new SubResource3();
        }
    }

    public static class SubResource3 {
        @GET
        @Produces("application/json")
        public SubResource3 retrieve() {
            return this;
        }
    }
}