package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@OpenAPIDefinition(info = @Info(description = "Subresource Class"))
@Path("subresource")
public class ClassPathSubResource {

    @GET
    @Operation(operationId = "get Code")
    public String getCode() {
        return "400";
    }

    @GET
    @Path("{id}")
    public String getWidget(@PathParam("id") String id) {
        return "widget";
    }
}