package resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import models.Namespace.Description;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("/basic")
public class ResourceWithInnerClass {

  @GET
  @Path("/description")
  @ApiOperation(value = "Get list of instances of inner class", response = Description.class, responseContainer = "list")
  public List<Description> getDescription() {
    return null;
  }
}
