package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.swagger.annotations.Api;

@Api("/employees")
@Path("/employees")
public class ResourceWithSubResourceNoApiAnnotation {
    @GET
    public SubResourceNoApiAnnotation getTest() {
        return new SubResourceNoApiAnnotation();
    }
}
