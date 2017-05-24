package io.swagger.jaxrs2.resources;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by RafaelLopez on 5/20/17.
 */
public class ParametersResource {

    @GET
    @Path("/")
    @Operation(parameters = {
            @Parameter(in = "path", name = "subscriptionId",
                    required = true, description = "parameter description",
                    allowEmptyValue = true, allowReserved = true,
                    schema = @Schema(
                            type = "string",
                            format = "uuid",
                            description = "the generated UUID",
                            readOnly = true)
            )}
    )
    public void getParameters() {
    }
}
