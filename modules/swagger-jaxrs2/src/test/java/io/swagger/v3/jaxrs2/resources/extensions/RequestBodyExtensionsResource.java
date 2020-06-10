package io.swagger.v3.jaxrs2.resources.extensions;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class RequestBodyExtensionsResource {
    @GET
    @Path("/user")
    @Operation(operationId = "Id"
    )
    public void getUser(
            @Parameter(description = "Parameter with no IN", required = true)
            @RequestBody(description = "Request Body in Param",
            extensions = {
                    @Extension(name = "x-extension", properties = {
                            @ExtensionProperty(name = "name", value = "param")}),
                    @Extension(name = "x-extension2", properties = {
                            @ExtensionProperty(name = "another", value = "val")})})
                    User user) {
    }
}
