package io.swagger.v3.jaxrs2.petstore.parameter;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Class with a multiple annotated.
 */
public class ComplexParameterResource {

    @Parameter(description = "Phone definied in Field")
    private String phone;

    public ComplexParameterResource(@Parameter(description = "phone Param", name = "phone") final String phone) {
        this.phone = phone;
    }

    @GET
    @Path("/complexparameter")
    @Operation(operationId = "create User")
    public User findUser(@Parameter(description = "idParam") @QueryParam("id") final String id,
                         final String name, @QueryParam("lastName") final String lastName,
                         @Parameter(description = "address", schema = @Schema(implementation = User.class))
                         @QueryParam("address") final String address) {
        return new User();
    }
}
