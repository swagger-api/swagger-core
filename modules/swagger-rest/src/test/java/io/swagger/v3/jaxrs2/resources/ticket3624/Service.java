package io.swagger.v3.jaxrs2.resources.ticket3624;

import io.swagger.v3.jaxrs2.resources.ticket3624.model.ByIdResponse;
import io.swagger.v3.jaxrs2.resources.ticket3624.model.ContainerizedResponse;
import io.swagger.v3.jaxrs2.resources.ticket3624.model.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Tag(name = "ExampleService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(Service.ROOT_PATH)
public interface Service {
    String ROOT_PATH = "/example";

    @GET
    @Path("/model")
    @Operation(summary = " Retrieve models for display to the user")
    Response getModels();


    @GET
    @Path("/model/by/ids")
    @Operation(summary = " Retrieve models by their ids")
    ByIdResponse getModelsById();

    @GET
    @Path("/containerized/model")
    @Operation(summary = " Retrieve review insights for a specific product")
    ContainerizedResponse getContainerizedModels();

}
