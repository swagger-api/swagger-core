package io.swagger.resources;



import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public class SimpleSelfReferencingSubResource {

    @Path("/sub")
    @ApiOperation(value = "retrieveSubResource")
    public SubResource retrieveSubResource() {
        return new SubResource();
    }

    public static class SubResource {

        @GET
        @Produces("application/json")
        @ApiOperation(value = "retrieve")
        public SubResource retrieve() {
            return this;
        }

        @Path("/recurse")
        @ApiOperation(value = "retrieveSelf")
        public SubResource retrieveSelf() {
            return this;
        }
        @Path("/recurse2")
        @ApiOperation(value = "retrieveSelf2")
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
        @ApiOperation(value = "retrieve")
        public SubResource2 retrieve() {
            return this;
        }

        @Path("/recurse")
        @ApiOperation(value = "retrieveSelf")
        public SubResource2 retrieveSelf() {
            return this;
        }
        @Path("/recurse1")
        @ApiOperation(value = "retrieveSelf1")
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