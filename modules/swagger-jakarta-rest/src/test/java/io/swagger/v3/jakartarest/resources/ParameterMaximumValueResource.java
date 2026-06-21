package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
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
