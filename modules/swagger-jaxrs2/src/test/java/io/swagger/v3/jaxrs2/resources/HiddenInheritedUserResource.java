package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HiddenInheritedUserResource {

    @Hidden
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Internal {
    }

    @Path("/user")
    public interface UserApi {

        @GET
        @Path("/internal")
        @Hidden
        @Operation(summary = "Internal user data")
        Response internalUser();

        @GET
        @Path("/public")
        @Operation(summary = "Public user data")
        Response publicUser();
    }

    public static class UserApiImpl implements UserApi {

        @Override
        public Response internalUser() {
            return Response.ok().build();
        }

        @Override
        public Response publicUser() {
            return Response.ok().build();
        }
    }

    @Hidden
    @Path("/admin")
    public interface AdminApi {

        @GET
        @Operation(summary = "Admin data")
        Response admin();
    }

    public static class AdminApiImpl implements AdminApi {

        @Override
        public Response admin() {
            return Response.ok().build();
        }
    }

    @Internal
    @Path("/metrics")
    public interface MetricsApi {

        @GET
        @Operation(summary = "Metrics data")
        Response metrics();
    }

    public static class MetricsApiImpl implements MetricsApi {

        @Override
        public Response metrics() {
            return Response.ok().build();
        }
    }
}
