package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("resources")
@Tag(name = "resource")
public interface Ticket2644AnnotatedInterface {
    @GET
    Response getResource();
}

