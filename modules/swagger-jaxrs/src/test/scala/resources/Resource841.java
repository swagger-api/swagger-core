package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(value = "/external/info/")
@Path("fun")
public class Resource841 {
  @ApiOperation(value="this", tags={@Tag(value = "tag1", description = "the tag 1")})
  @GET
  @Path("/this")
  public Response getThis(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return Response.ok().build();
  }

  @ApiOperation(value="that", tags={@Tag(value = "tag2", description = "the tag 2")})
  @GET
  @Path("/that")
  public Response getThat(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return Response.ok().build();
  }

  @ApiOperation(value="everything", tags={
    @Tag(value = "tag1", description = "the tag 1"),
    @Tag(value = "tag2", description = "the tag 2")})
  @GET
  public Response getEverything(@ApiParam(value = "test") ArrayList<String> tenantId) {
    return Response.ok().build();
  }
}