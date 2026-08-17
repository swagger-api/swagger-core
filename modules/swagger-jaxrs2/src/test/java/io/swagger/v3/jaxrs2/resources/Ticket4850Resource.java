package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/bar")
public class Ticket4850Resource {
    @GET
    @Path("")
    public ExtensionsResource test(

    ) {return new ExtensionsResource();}

    @Schema(
            description = "ExtensionsResource",
            extensions = {
                    @Extension(name = "x-user", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "user-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
            }
    )
    private class ExtensionsResource {
        public ExtensionsResource() {}
    }
}
