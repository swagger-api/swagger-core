package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.testng.annotations.Test;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Tests for issue #4928 - Validation Grouping support in @Operation.
 * Verifies that groups() in @Operation is serialized as x-groups extension.
 */
public class OperationGroupsTest {

    // --- Validation group marker interfaces ---
    interface Create {}
    interface Update {}

    // --- Test JAX-RS resource ---
    @Path("/users")
    static class UserResource {

        @POST
        @Operation(summary = "Create user", groups = Create.class)
        public void create() {}

        @PUT
        @Operation(summary = "Update user", groups = Update.class)
        public void update() {}

        @javax.ws.rs.GET
        @Operation(summary = "Get user")   // no groups -> no x-groups extension
        public void get() {}
    }

    @Test(description = "@Operation groups() should be written as x-groups extension on POST operation")
    public void testOperationGroupsCreate() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(UserResource.class);

        assertNotNull(openAPI.getPaths(), "paths must not be null");
        PathItem pathItem = openAPI.getPaths().get("/users");
        assertNotNull(pathItem, "/users path must exist");
        assertNotNull(pathItem.getPost(), "POST operation must exist");

        Object groupsExt = pathItem.getPost().getExtensions().get("x-groups");
        assertNotNull(groupsExt, "x-groups extension must be set on POST");
        assertEquals(groupsExt.toString(), "Create",
                "POST should have x-groups=Create");
    }

    @Test(description = "@Operation groups() should be written as x-groups extension on PUT operation")
    public void testOperationGroupsUpdate() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(UserResource.class);

        PathItem pathItem = openAPI.getPaths().get("/users");
        assertNotNull(pathItem.getPut(), "PUT operation must exist");

        Object groupsExt = pathItem.getPut().getExtensions().get("x-groups");
        assertNotNull(groupsExt, "x-groups extension must be set on PUT");
        assertEquals(groupsExt.toString(), "Update",
                "PUT should have x-groups=Update");
    }

    @Test(description = "No groups() on @Operation should not add x-groups extension")
    public void testOperationNoGroupsHasNoExtension() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(UserResource.class);

        PathItem pathItem = openAPI.getPaths().get("/users");
        assertNotNull(pathItem.getGet(), "GET operation must exist");

        // Either no extensions at all, or x-groups not present
        if (pathItem.getGet().getExtensions() != null) {
            assertNull(pathItem.getGet().getExtensions().get("x-groups"),
                    "GET should NOT have x-groups when no groups() is set");
        }
    }
}
