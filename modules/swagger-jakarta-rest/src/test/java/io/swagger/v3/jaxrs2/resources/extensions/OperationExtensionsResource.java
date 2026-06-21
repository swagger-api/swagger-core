package io.swagger.v3.jaxrs2.resources.extensions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

public class OperationExtensionsResource {

    @GET
    @Path("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description",
            extensions = {
                    @Extension(name = "x-operation", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "x-operation-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
                    })
    public Response getSummaryAndDescription() {
        return Response.ok().entity("ok").build();
    }


}
