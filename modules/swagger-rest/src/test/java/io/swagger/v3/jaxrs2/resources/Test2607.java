package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@OpenAPIDefinition(info =
@Info( title = "TEST", version = "0.0", description = "Test API" ) )
@Path("/swaggertest/")
public class Test2607 {

    @GET
    @Path("name")
    @Produces(MediaType.TEXT_PLAIN)
    public String getName() {
        return "SwaggerTest";
    }

    @Path("subresource")
    @Operation
    public TestSub2607 getSubResource() {
        return new TestSub2607();
    }

}