package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/test")
public class ParameterMaximumValueResource {

    @GET
    @Path("/{petId}")
    public Response getPetById(
            @Parameter(
                    description = "ID of pet that needs to be fetched",
                    schema = @Schema(
                            type = "integer",
                            format = "int64",
                            exclusiveMinimumValue = 1,
                            exclusiveMaximumValue = 10
                    ),
                    required = true)
            @PathParam("petId") Long petId) {
        return null;
    }
}
