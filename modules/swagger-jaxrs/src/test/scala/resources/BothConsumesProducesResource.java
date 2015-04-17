package resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import models.Sample;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/basic", description = "Basic resource", produces = MediaType.APPLICATION_ATOM_XML, consumes = MediaType.APPLICATION_XHTML_XML)
@Produces({"application/xml"})
@Consumes({"application/yaml"})
public class BothConsumesProducesResource {
  
  @GET
  @Path("/{id}")
  @ApiOperation(value = "Get object by ID",
    notes = "No details provided",
    response = Sample.class,
    position = 0)
  public Response noConsumesProduces(){
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
  public Response bothConsumesProduces(){
    return Response.ok().entity("ok").build();
  }

  @GET
  @Path("/{id}/{name}/value")
  @Produces("text/html")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get simple string value",
    notes = "No details provided",
    response = String.class,
    position = 0,
    produces = "text/plain")
  public Response oneConsumesBothProduces(){
    return Response.ok().entity("ok").build();
  }

  @GET
  @Path("/{id}/{type}/value")
  @Produces("text/html")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get simple string value",
    notes = "No details provided",
    response = String.class,
    position = 0,
    consumes = "application/xml")
  public Response bothConsumesOneProduces(){
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
}