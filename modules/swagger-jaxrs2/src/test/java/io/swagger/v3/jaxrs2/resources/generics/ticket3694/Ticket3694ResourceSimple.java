package io.swagger.v3.jaxrs2.resources.generics.ticket3694;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
public class Ticket3694ResourceSimple implements Ticket3694ResourceInterfaceSimple<String> {



    @POST
    @Path("bar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation
    @Override
    public String bar(List<String> foo) {
        return null;
    }

}
