package resources;

import io.swagger.annotations.Api;

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

  // injection into a constructor parameter
  public ResourceWithKnownInjections(@PathParam("id") Integer constructorParam) {
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

  @Path("/subresource")
  public SubResource subResourceLocator(@QueryParam("subResourceParam") String subResourceParam) {
    // injection into a sub resource locator parameter
    return new SubResource(subResourceParam);
  }

  @Context
  public void setRequest(Request request) {
    // injection into a setter method
  }

  @Api(description = "Sub resource")
  public static class SubResource {

    private String subResourceParam;

    public SubResource(String subResourceParam) {
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
