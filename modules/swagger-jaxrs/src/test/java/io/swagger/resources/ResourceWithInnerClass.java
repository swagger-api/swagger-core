package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Namespace.Description;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Api("/basic")
@Path("/")
public class ResourceWithInnerClass {

    @GET
    @Path("/description")
    @ApiOperation(value = "Get list of instances of inner class", response = Description.class, responseContainer = "list")
    public List<Description> getDescription() {
        return null;
    }
}
