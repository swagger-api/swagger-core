package io.swagger.v3.jaxrs2.it.resources;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.Explode.FALSE;
import static io.swagger.v3.oas.annotations.enums.Explode.TRUE;

@Path("/things")
@Produces("application/json")
public class UrlEncodedResourceWithEncodings {

    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RequestBody(content = @Content(encoding = {@Encoding(name = "id", style = "FORM", explode = true),
            @Encoding(name = "name", style = "FORM", explode = true)}) )
    public Response searchForThings( @BeanParam CompositeFormBody body ) {
        return Response.status(200).entity("Searching for something").build();
    }

    @POST
    @Path("/sriracha")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RequestBody(content = @Content(encoding = {@Encoding(name = "id", style = "FORM", explode = true),
             @Encoding(name = "name", style = "FORM", explode = true )}))
    public Response srirachaThing(@FormParam( "id" ) List<String> ids, @FormParam( "name" ) List<String> names) {
        return Response.status(200).entity("Sriracha!").build();
    }

    public static class CompositeFormBody {
        @Parameter(name = "id", style = ParameterStyle.FORM, explode = TRUE)
        @FormParam("id")
        public List<String> ids;

        @Parameter(name = "name", style = ParameterStyle.FORM, explode = FALSE)
        @FormParam("name")
        public List<String> names;
    }
}
