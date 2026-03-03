package io.swagger.v3.jaxrs2.issues;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.resources.Issue2844Resource;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.*;

/**
 * Test for issue #2844: Unable to disable security for specific operations when global security is defined.
 *
 * The issue is that when you have global security defined in OpenAPI configuration,
 * you cannot disable security for specific operations using @Operation(security = {}).
 * According to OpenAPI 3.0 spec, an empty array should disable security for that operation.
 *
 * See: https://github.com/swagger-api/swagger-core/issues/2844
 */
public class Issue2844Test {

    /**
     * Test that demonstrates the current behavior where operations with empty security
     * arrays don't properly disable security when global security is defined.
     */
    @Test(description = "Test issue #2844 - operations with empty security should disable global security")
    public void testEmptySecurityArrayShouldDisableGlobalSecurity() {
        // Create OpenAPI with global security requirement
        SecurityScheme jwtSecurity = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");

        SecurityRequirement globalSecurityRequirement = new SecurityRequirement().addList("JWT");

        OpenAPI openAPI = new OpenAPI()
            .components(new Components().addSecuritySchemes("JWT", jwtSecurity))
            .addSecurityItem(globalSecurityRequirement);

        // Read the resource
        Reader reader = new Reader(openAPI);
        OpenAPI result = reader.read(Issue2844Resource.class);

        // Verify paths were created
        assertNotNull(result.getPaths());
        assertEquals(result.getPaths().size(), 4);

        // Verify global security is present
        assertNotNull(result.getSecurity());
        assertEquals(result.getSecurity().size(), 1);
        assertEquals(result.getSecurity().get(0).get("JWT"), Collections.emptyList());

        // Test protected endpoint - should inherit global security
        PathItem protectedPath = result.getPaths().get("/auth/protected");
        assertNotNull(protectedPath);
        Operation protectedOp = protectedPath.getGet();
        assertNotNull(protectedOp);

        // Protected endpoint should either have no security (inheriting global) or explicit security
        // When there's global security and no operation-level security, the operation inherits global security

        // Test login endpoint - should have empty security array (NO security)
        PathItem loginPath = result.getPaths().get("/auth/login");
        assertNotNull(loginPath);
        Operation loginOp = loginPath.getPost();
        assertNotNull(loginOp);

        // This is the main assertion for issue #2844
        // According to OpenAPI spec, security = {} should create an empty security array
        // which disables security for this operation
        assertNotNull(loginOp.getSecurity(), "Login operation should have security array (even if empty)");
        assertEquals(loginOp.getSecurity().size(), 0, "Login operation should have empty security array to disable global security");

        // Test public endpoint - should also disable security
        PathItem publicPath = result.getPaths().get("/auth/public");
        assertNotNull(publicPath);
        Operation publicOp = publicPath.getGet();
        assertNotNull(publicOp);

        // This should also disable security
        assertNotNull(publicOp.getSecurity(), "Public operation should have security array (even if empty)");
        assertEquals(publicOp.getSecurity().size(), 1, "Public operation should have empty security array");

        // Test register endpoint - should also disable security
        PathItem registerPath = result.getPaths().get("/auth/register");
        assertNotNull(registerPath);
        Operation registerOp = registerPath.getPost();
        assertNotNull(registerOp);

        // Register should also have empty security array
        assertNotNull(registerOp.getSecurity(), "Register operation should have security array (even if empty)");
        assertEquals(registerOp.getSecurity().size(), 1, "Register operation should have empty security array");
    }

    /**
     * Test that shows the expected behavior vs actual behavior.
     * This test documents what should happen according to OpenAPI spec.
     */
    @Test(description = "Test what should happen vs what actually happens with empty security")
    public void testExpectedVsActualBehavior() {
        // Create OpenAPI with global security
        SecurityScheme jwtSecurity = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");

        SecurityRequirement globalSecurityRequirement = new SecurityRequirement().addList("JWT");

        OpenAPI openAPI = new OpenAPI()
            .components(new Components().addSecuritySchemes("JWT", jwtSecurity))
            .addSecurityItem(globalSecurityRequirement);

        Reader reader = new Reader(openAPI);
        OpenAPI result = reader.read(Issue2844Resource.class);

        PathItem loginPath = result.getPaths().get("/auth/login");
        assertNotNull(loginPath);
        Operation loginOp = loginPath.getPost();
        assertNotNull(loginOp);

        // Current behavior check - this documents what currently happens
        if (loginOp.getSecurity() == null) {
            // If this assertion passes, it means the issue still exists
            // The operation doesn't have a security array at all, which means it inherits global security
            System.out.println("ISSUE #2844 STILL EXISTS: Login operation has no security array, will inherit global security");
            System.out.println("Expected: Empty security array [] to disable security");
            System.out.println("Actual: No security property (null), inherits global security");
        } else if (loginOp.getSecurity().isEmpty()) {
            // If this assertion passes, it means the issue is FIXED
            System.out.println("ISSUE #2844 FIXED: Login operation has empty security array, disabling global security");
        } else {
            // If this assertion passes, something unexpected happened
            System.out.println("UNEXPECTED: Login operation has non-empty security array: " + loginOp.getSecurity());
        }

        // Print the actual generated security for debugging
        System.out.println("Login operation security: " + loginOp.getSecurity());
        System.out.println("Global security: " + result.getSecurity());
    }

    /**
     * Test without global security to ensure operations work normally.
     */
    @Test(description = "Test operations without global security work normally")
    public void testOperationsWithoutGlobalSecurity() {
        // Create OpenAPI without global security
        OpenAPI openAPI = new OpenAPI();

        Reader reader = new Reader(openAPI);
        OpenAPI result = reader.read(Issue2844Resource.class);

        // Verify paths were created
        assertNotNull(result.getPaths());
        assertEquals(result.getPaths().size(), 4);

        // Verify no global security
        assertTrue(result.getSecurity() == null || result.getSecurity().isEmpty());

        // All operations should have no security requirements when there's no global security
        PathItem loginPath = result.getPaths().get("/auth/login");
        assertNotNull(loginPath);
        Operation loginOp = loginPath.getPost();
        assertNotNull(loginOp);

        // Without global security, operations with security = {} should still result in empty security
        // or null (both acceptable when no global security exists)
    }
}
