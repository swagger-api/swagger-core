package io.swagger.v3.jaxrs2.resources.extensions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public class ParameterExtensionsResource {

    @GET
    @Path("/")
    @Operation(operationId = "Id")
    public void getParameters(@Parameter(extensions = @Extension(name = "x-parameter", properties = {
            @ExtensionProperty(name = "parameter", value = "value")}))
                              @QueryParam("subscriptionId") String subscriptionId) {
    }

}
