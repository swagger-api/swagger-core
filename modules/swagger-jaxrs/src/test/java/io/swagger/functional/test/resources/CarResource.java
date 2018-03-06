package io.swagger.functional.test.resources;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.functional.test.model.Car;
import io.swagger.functional.test.view.Detail;
import io.swagger.functional.test.view.Sale;
import io.swagger.functional.test.view.Summary;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/cars")
@Api(tags = "cars", description = "Car operations")
@Produces("application/json")
@Consumes("application/json")
public class CarResource {

  @GET
  @Path("/summary")
  @JsonView({Summary.class})
  @ApiOperation(value = "Return car summaries", response = Car.class, consumes = "List")
  public Response getSummaries() {
    return Response.ok(Arrays.asList(new Car())).build();
  }

  @GET
  @Path("/detail")
  @JsonView({Detail.class})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Return car detail", response = Car.class, responseContainer = "List")
  })
  @ApiOperation(value = "Return car detail")
  public List<Car> getDetails() {
    return Arrays.asList(new Car());
  }

  @GET
  @Path("/sale")
  @JsonView({Summary.class, Sale.class})
  @ApiOperation(value = "Return car sale summary")
  public List<Car> getSaleSummaries() {
    return Arrays.asList(new Car());
  }

  @GET
  @Path("/all")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Return whole car", response = Car.class, responseContainer = "List")
  })
  @ApiOperation(value = "Return whole car")
  public List<Car> getAll() {
    return Arrays.asList(new Car());
  }

}
