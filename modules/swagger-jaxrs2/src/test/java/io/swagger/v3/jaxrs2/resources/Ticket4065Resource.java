package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/bar")
public class Ticket4065Resource {
    @GET
    @Path("")
    @Produces({"application/json"})
    public void test(
            @Parameter(in = ParameterIn.QUERY, name = "blub", explode = Explode.FALSE) Long[] ids
    ) {}
}
