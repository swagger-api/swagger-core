package resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import models.EmptyModel;

import javax.ws.rs.*;

@Api("/test")
@Path("/test")
public class ResourceWithEmptyModel {
  @ApiOperation(value="test", response = EmptyModel.class)
  @GET
  public void getTest(@ApiParam(value = "test") @QueryParam("test") Integer param) {
    return;
  }
}
