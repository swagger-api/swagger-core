package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public class ParametersResource {

    @GET
    @Path("/")
    @Operation(parameters = {
            @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
                    required = true, description = "parameter description",
                    allowEmptyValue = true, allowReserved = true,
                    schema = @Schema(
                            type = "string",
                            format = "uuid",
                            description = "the generated UUID",
                            accessMode = Schema.AccessMode.READ_ONLY)
            )}
    )
    public void getParameters(@QueryParam("subscriptionId") String subscriptionId) {
    }
}
