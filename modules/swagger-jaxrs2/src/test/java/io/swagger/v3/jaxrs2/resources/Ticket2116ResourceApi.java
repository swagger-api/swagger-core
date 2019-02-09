package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(title = "TEST", version = "0.0", description = "Test API")
)
@Path("/swaggertest/")
public interface Ticket2116ResourceApi {

    @GET
    @Path("name")
    @Produces(MediaType.TEXT_PLAIN)
    String getName();

    @Path("subresource")
    @Operation
    Ticket2116SubResourceApi getSubResource();

    @Hidden
    @Path("hidden-subresource")
    @Operation
    Ticket2116SubResourceApi getAnotherSubResource();

}