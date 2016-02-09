package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource")
@Produces({"application/xml"})
@Consumes({"application/yaml"})
@Path("/")
public class RsConsumesProducesResource {

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get object by ID",
            notes = "No details provided",
            response = Sample.class,
            position = 0)
    public Response noConsumesProduces() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/{id}/value")
    @Produces("text/html")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get simple string value",
            notes = "No details provided",
            response = String.class,
            position = 0,
            produces = "text/plain",
            consumes = "application/xml")
    public Response bothConsumesProduces() {
        return Response.ok().entity("ok").build();
    }

    @PUT
    @Path("/{id}")
    @Produces({"text/plain"})
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update by ID",
            notes = "No details provided",
            position = 1)
    public Response rsConsumesProduces() {
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/value")
    @ApiOperation(value = "Update by ID",
            notes = "No details provided",
            position = 1,
            produces = "text/plain",
            consumes = "application/xml")
    public Response apiConsumesProduces() {
        return Response.ok().build();
    }

    @PUT
    @Path("/split")
    @Produces({"image/jpeg,image/gif", "image/png,"})
    @Consumes({"image/jpeg,,image/gif", "image/png"})
    @ApiOperation(value = "Get simple string value",
            notes = "No details provided",
            response = String.class)
    public Response splitRsConsumesProduces() {
        return Response.ok().build();
    }

}