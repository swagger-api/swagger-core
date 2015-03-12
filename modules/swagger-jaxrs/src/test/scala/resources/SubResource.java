package resources;

import com.wordnik.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;

@Api(hidden = true)
@Path("/{id}")
public class SubResource {
  @ApiOperation(value="gets an object by ID", tags = {@Tag("Employees")})
  @GET
  public void getSubresourceOperation(@ApiParam(value = "test") Long userId) {
    return;
  }
}