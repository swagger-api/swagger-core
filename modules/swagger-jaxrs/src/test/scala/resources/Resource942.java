package resources;

import models.Employee;
import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api("/test")
@Path("/test")
public class Resource942 {
  @ApiOperation(value="test")
  @POST
  public void addTest(@ApiParam(required=false) Employee employee) {
    return;
  }

  @GET
  public Resource942SubResource getSub() {
    return new Resource942SubResource();
  }
}