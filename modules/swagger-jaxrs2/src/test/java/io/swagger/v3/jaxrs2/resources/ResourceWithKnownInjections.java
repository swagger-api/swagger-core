package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Path("/resource/{id}")
@Produces({"application/json", "application/xml"})
public class ResourceWithKnownInjections {

    private final Integer constructorParam;
    @QueryParam("fieldParam")
    private String fieldParam; // injection into a class field

    @Parameter(hidden = true)
    @QueryParam("hiddenParam")
    private String hiddenParam;

    public ResourceWithKnownInjections(@PathParam("id") final Integer constructorParam, @QueryParam("hiddenParam") @Parameter(hidden = true) final String hiddenParam) {
        this.constructorParam = constructorParam;
    }

    @GET
    @Operation
    public String get(@QueryParam("methodParam") final String methodParam) {
        // injection into a resource method parameter
        final StringBuilder sb = new StringBuilder();
        sb.append("Constructor param: ").append(constructorParam).append("\n");
        sb.append("Field param: ").append(fieldParam).append("\n");
        sb.append("Method param: ").append(methodParam).append("\n");
        return sb.toString();
    }
}
