package resources;

import models.EmptyModel;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api("/test")
@Path("/test")
public class ResourceWithEmptyModel {
  @ApiOperation(value="test", response = EmptyModel.class)
  @GET
  public void getTest(@ApiParam(value = "test") @QueryParam("test") Integer param) {
    return;
  }
}
