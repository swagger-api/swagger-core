package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;

import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.Explode.FALSE;
import static io.swagger.v3.oas.annotations.enums.Explode.TRUE;

@Path("/things")
@Produces("application/json")
public class UrlEncodedResourceWithEncodings {

    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response searchForThings( @BeanParam CompositeFormBody body ) {
        return Response.status(200).entity("Searching for something").build();
    }

    @POST
    @Path("/sriracha")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response srirachaThing(
            @Parameter(name = "id", style = ParameterStyle.FORM, explode = TRUE, description = "id param") @FormParam( "id" ) List<String> ids,
            @Parameter(name = "name", style = ParameterStyle.FORM, explode = FALSE) @FormParam( "name" ) List<String> names) {
        return Response.status(200).entity("Sriracha!").build();
    }

    public static class CompositeFormBody {
        @Parameter(name = "id", style = ParameterStyle.FORM, explode = TRUE, description = "id param")
        @FormParam("id")
        public List<String> ids;

        @Parameter(name = "name", style = ParameterStyle.FORM, explode = FALSE)
        @FormParam("name")
        public List<String> names;
    }
}
