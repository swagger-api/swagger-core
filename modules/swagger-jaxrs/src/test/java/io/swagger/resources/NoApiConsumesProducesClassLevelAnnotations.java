package io.swagger.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.Sample;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Produces({"application/xml"})
@Consumes({"application/yaml"})
@Path("/")
public class NoApiConsumesProducesClassLevelAnnotations {
    
    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            position = 0)
    public Response noConsumesProduces() {
        return Response.ok().entity("ok").build();
    }

}
