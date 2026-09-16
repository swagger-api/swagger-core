package io.swagger.v3.jaxrs2.resources.generics.ticket3694;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class Ticket3694Resource implements Ticket3694ResourceInterface<String> {


    @POST
    @Path("foo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Foo List in Interface", tags = "Foo")
    @Override
    public Response foo(List<String> foo) {
        return null;
    }

    @POST
    @Path("bar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation
    @Override
    public String bar(List<String> foo) {
        return null;
    }

    @POST
    @Path("another")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation
    @Override
    public Response another(String foo) {
        return null;
    }
}
