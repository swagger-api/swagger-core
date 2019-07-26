package io.swagger.v3.jaxrs2.resources;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.tags.Tag;

@Path("resources")
@Tag(name = "resource")
public interface Ticket3253AnnotatedInterface<T extends Number> {
    @POST
    @Path("list")
    Response postResource(List<T> payload);

    @POST
    @Path("single")
    Response postResource(T payload);
}