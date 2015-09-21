package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

@Path("/resource/{id}")
@Api(value = "/resource", description = "Summary of injections resource")
@Produces({"application/json", "application/xml"})
public class ResourceWithKnownInjections {

    private Integer constructorParam;
    @QueryParam("fieldParam")
    private String fieldParam; // injection into a class field
    @ApiParam(hidden = true)
    @QueryParam("hiddenParam")
    private String hiddenParam;

    // injection into a constructor parameter
    public ResourceWithKnownInjections(@PathParam("id") Integer constructorParam,
                                       @ApiParam(hidden = true) @QueryParam("hiddenParam") String hiddenParam,
                                       @Context ServletConfig context) {
        this.constructorParam = constructorParam;
    }

    private ResourceWithKnownInjections(@PathParam("id") Integer constructorParam, @QueryParam("fakeParam") String
            fakeParam) {
        this.constructorParam = constructorParam;
    }

    @GET
    public String get(@QueryParam("methodParam") String methodParam) {
        // injection into a resource method parameter
        final StringBuilder sb = new StringBuilder();
        sb.append("Constructor param: ").append(constructorParam).append("\n");
        sb.append("Field param: ").append(fieldParam).append("\n");
        sb.append("Method param: ").append(methodParam).append("\n");
        return sb.toString();
    }

    @Path("/subresource1")
    public SubResource1 subResourceLocator1(@QueryParam("subResourceParam") String subResourceParam) {
        // injection into a sub resource locator parameter
        return new SubResource1(subResourceParam);
    }

    @Path("/subresource2")
    public Class<SubResource2> subResourceLocator2(@QueryParam("subResourceParam") String subResourceParam) {
        // injection into a sub resource locator parameter
        return SubResource2.class;
    }

    @Path("/subresource3")
    public Class<SubResource3> subResourceLocator3(@QueryParam("subResourceParam") String subResourceParam) {
        // injection into a sub resource locator parameter
        return SubResource3.class;
    }

    @Context
    public void setRequest(Request request) {
        // injection into a setter method
    }

    @Api(description = "Sub resource 1")
    public static class SubResource1 {

        private String subResourceParam;

        public SubResource1(String subResourceParam) {
            this.subResourceParam = subResourceParam;
        }

        @GET
        public String get() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Sub Resource: ").append(subResourceParam);
            return sb.toString();
        }
    }

    @Api(description = "Sub resource 2")
    public static class SubResource2 {

        private String subResourceParam;

        public SubResource2(@QueryParam("subConstructorParam") String subResourceParam) {
            this.subResourceParam = subResourceParam;
        }

        @GET
        public String get() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Sub Resource: ").append(subResourceParam);
            return sb.toString();
        }
    }

    @Api(description = "Sub resource 3")
    public class SubResource3 {

        private String subResourceParam;

        public SubResource3(@QueryParam("subConstructorParam") String subResourceParam) {
            this.subResourceParam = subResourceParam;
        }

        @GET
        public String get() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Sub Resource: ").append(subResourceParam);
            return sb.toString();
        }
    }
}
