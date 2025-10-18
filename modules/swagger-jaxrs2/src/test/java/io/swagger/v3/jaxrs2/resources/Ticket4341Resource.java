package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

public class Ticket4341Resource {

    @GET
    @Path("/user")
    public User getUsers() {
        return null;
    }

    static class User {
        @ArraySchema(
                arraySchema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED),
                schema = @Schema(type = "string")
        )
        public List<String> requiredArray;

        @ArraySchema(
                arraySchema = @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED),
                schema = @Schema(type = "string")
        )
        public List<String> notRequiredArray;

        @ArraySchema(
                arraySchema = @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED),
                schema = @Schema(type = "string")
        )
        @NotNull
        public List<String> notRequiredArrayWithNotNull;

        @ArraySchema(
                arraySchema = @Schema(requiredMode = Schema.RequiredMode.AUTO),
                schema = @Schema(type = "string")
        )
        @NotNull
        public List<String> autoRequiredWithNotNull;

        @ArraySchema(
                arraySchema = @Schema(requiredMode = Schema.RequiredMode.AUTO),
                schema = @Schema(type = "string")
        )
        public List<String> autoNotRequired;
    }
}
