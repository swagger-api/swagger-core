package io.swagger.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Sample;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource",
        produces = MediaType.APPLICATION_ATOM_XML + "," + MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML,
        consumes = MediaType.APPLICATION_XHTML_XML + "," + MediaType.APPLICATION_XML + "," + MediaType.APPLICATION_JSON)
@Path("/")
public class ApiMultipleConsumesProducesResource {

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
