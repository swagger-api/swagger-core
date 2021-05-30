package io.swagger.v3.jaxrs2.resources.generics.ticket3694;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
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
