package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.models.Pagination;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/resource/{id}")
@Api(value = "/resource", description = "Summary of injections resource")
@Produces({"application/json", "application/xml"})
public class ResourceWithKnownInjections {

    private Integer constructorParam;
    @QueryParam("fieldParam")
    private String fieldParam; // injection into a class field
    @BeanParam
    private Pagination pagination;

    // injection into a constructor parameter
    @Inject
    public ResourceWithKnownInjections(Integer constructorParam) {
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
}
