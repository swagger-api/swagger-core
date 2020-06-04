package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.Path;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

@Path("test")
public class TicketRequiredPropsResource {

    @PUT
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void schemaImpl(
            @BeanParam
            @RequestBody(content = @Content(schema = @Schema(
                    name = "login",
                    requiredProperties = {"login", "password"}))) User user
    ) {
    }

    public static class User {

        @FormParam("login")
        @Schema(required = true, name = "login")
        private String login;

        @FormParam("password")
        @Schema(required = true, name = "password")
        private String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
