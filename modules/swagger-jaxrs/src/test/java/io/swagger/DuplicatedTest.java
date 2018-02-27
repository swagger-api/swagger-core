package io.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.jaxrs.Reader;
import io.swagger.models.Swagger;
import io.swagger.util.Json;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DuplicatedTest {

  @Api
  @Path("/")
  private static class OrderApi {

    @GET
    @Path("/orders/summary")
    public List<io.swagger.models.issue2121.summary.Order> getOrderSummaries() {
      return Arrays.asList(new io.swagger.models.issue2121.summary.Order());
    }

    @GET
    @Path("/orders/detail")
    public List<io.swagger.models.issue2121.detail.Order> getOrderDetails() {
      return Arrays.asList(new io.swagger.models.issue2121.detail.Order());
    }

    @GET
    @Path("/orders/summary/dto")
    public List<io.swagger.models.issue2121.OrderDto.Summary> getOrderDtoSummaries() {
      return Arrays.asList(new io.swagger.models.issue2121.OrderDto.Summary());
    }

    @GET
    @Path("/orders/detail/dto")
    public List<io.swagger.models.issue2121.OrderDto.Detail> getOrderDtoDetails() {
      return Arrays.asList(new io.swagger.models.issue2121.OrderDto.Detail());
    }

  }

  @Test(description = "check awareness of JsonView")
  public void shouldShowBothModel() throws JsonProcessingException {
    Swagger swagger = getSwagger(OrderApi.class);
    String swaggerJson = Json.mapper().writeValueAsString(swagger);

    Assert.assertTrue(swaggerJson.contains(io.swagger.models.issue2121.summary.Order.class.getName()));
    Assert.assertTrue(swaggerJson.contains(io.swagger.models.issue2121.detail.Order.class.getName()));
    Assert.assertTrue(swaggerJson.contains(io.swagger.models.issue2121.OrderDto.Summary.class.getName()));
    Assert.assertTrue(swaggerJson.contains(io.swagger.models.issue2121.OrderDto.Detail.class.getName()));

  }

  private Swagger getSwagger(Class<?> cls) {
    return new Reader(new Swagger()).read(cls);
  }

}
