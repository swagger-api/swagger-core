package io.swagger.v3.jaxrs2.resources.ticket3253;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.tags.Tag;

@Path("nums")
public interface Ticket3253Interface<A extends Number> {
    @POST
    @Path("single")
    Response postResource(A payload);

    @POST
    @Path("multiple")
    Response postResource(List<A> payload);
}
