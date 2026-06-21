package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/bar")
public class Ticket4065Resource {
    @GET
    @Path("")
    @Produces({"application/json"})
    public void test(
            @Parameter(in = ParameterIn.QUERY, name = "blub", explode = Explode.FALSE) Long[] ids
    ) {}
}
