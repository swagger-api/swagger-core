package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("resources")
@Tag(name = "resource")
public interface Ticket2644AnnotatedInterface {
    @GET
    Response getResource();
}

