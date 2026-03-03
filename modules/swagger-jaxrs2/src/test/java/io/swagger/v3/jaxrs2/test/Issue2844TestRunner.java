package io.swagger.v3.jaxrs2.test;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.resources.Issue2844Resource;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.Collections;

/**
 * Simple test runner for Issue #2844 to verify the behavior of empty security arrays.
 */
public class Issue2844TestRunner {
    public static void main(String[] args) {
        try {
            System.out.println("=== Testing Issue #2844: Empty Security Arrays ===");

            // Create OpenAPI with global security requirement
            SecurityScheme jwtSecurity = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

            SecurityRequirement globalSecurityRequirement = new SecurityRequirement().addList("JWT");

            OpenAPI openAPI = new OpenAPI()
                .components(new Components().addSecuritySchemes("JWT", jwtSecurity))
                .addSecurityItem(globalSecurityRequirement);

            System.out.println("Created OpenAPI with global security: " + openAPI.getSecurity());

            // Read the resource
            Reader reader = new Reader(openAPI);
            OpenAPI result = reader.read(Issue2844Resource.class);

            System.out.println("Paths found: " + (result.getPaths() != null ? result.getPaths().keySet() : "none"));
            System.out.println("Global security in result: " + result.getSecurity());

            // Test login endpoint - should have empty security array (NO security)
            PathItem loginPath = result.getPaths().get("/auth/login");
            if (loginPath != null) {
                Operation loginOp = loginPath.getPost();
                if (loginOp != null) {
                    System.out.println("\n=== LOGIN ENDPOINT ANALYSIS ===");
                    System.out.println("Login operation security: " + loginOp.getSecurity());

                    if (loginOp.getSecurity() == null) {
                        System.out.println("❌ ISSUE #2844 EXISTS: Login operation has no security array, will inherit global security");
                        System.out.println("   Expected: Empty security array [] to disable security");
                        System.out.println("   Actual: No security property (null), inherits global security");
                    } else if (loginOp.getSecurity().isEmpty()) {
                        System.out.println("✅ ISSUE #2844 FIXED: Login operation has empty security array, disabling global security");
                    } else {
                        System.out.println("❌ UNEXPECTED: Login operation has non-empty security array: " + loginOp.getSecurity());
                    }
                } else {
                    System.out.println("❌ Login POST operation not found");
                }
            } else {
                System.out.println("❌ Login path not found");
            }

            // Test public endpoint
            PathItem publicPath = result.getPaths().get("/auth/public");
            if (publicPath != null) {
                Operation publicOp = publicPath.getGet();
                if (publicOp != null) {
                    System.out.println("\n=== PUBLIC ENDPOINT ANALYSIS ===");
                    System.out.println("Public operation security: " + publicOp.getSecurity());

                    if (publicOp.getSecurity() == null) {
                        System.out.println("❌ Public operation has no security array, will inherit global security");
                    } else if (publicOp.getSecurity().isEmpty()) {
                        System.out.println("✅ Public operation has empty security array, disabling global security");
                    } else {
                        System.out.println("❌ Public operation has global security array: " + publicOp.getSecurity());
                    }
                }
            }

            // Test register endpoint
            PathItem registerPath = result.getPaths().get("/auth/register");
            if (registerPath != null) {
                Operation registerOp = registerPath.getPost();
                if (registerOp != null) {
                    System.out.println("\n=== REGISTER ENDPOINT ANALYSIS ===");
                    System.out.println("Register operation security: " + registerOp.getSecurity());

                    if (registerOp.getSecurity() == null) {
                        System.out.println("❌ Register operation has no security array, will inherit global security");
                    } else if (registerOp.getSecurity().isEmpty()) {
                        System.out.println("❌ Register operation has empty security array, disabling global security");
                    } else {
                        System.out.println("✅ Register operation has GLOBAL security array: " + registerOp.getSecurity());
                    }
                }
            }

            // Test protected endpoint (should inherit global security)
            PathItem protectedPath = result.getPaths().get("/auth/protected");
            if (protectedPath != null) {
                Operation protectedOp = protectedPath.getGet();
                if (protectedOp != null) {
                    System.out.println("\n=== PROTECTED ENDPOINT ANALYSIS ===");
                    System.out.println("Protected operation security: " + protectedOp.getSecurity());

                    if (protectedOp.getSecurity() == null) {
                        System.out.println("❌ Protected operation has no security array, correctly inherits global security");
                    } else {
                        System.out.println("✅ Protected operation has GLOBAL security array: " + protectedOp.getSecurity());
                    }
                }
            }

            System.out.println("\n=== SUMMARY ===");
            System.out.println("According to OpenAPI 3.0 spec, operations with security = {} should have an empty security array");
            System.out.println("that disables the global security requirement for that specific operation.");
            System.out.println("See: https://github.com/swagger-api/swagger-core/issues/2844");

        } catch (Exception e) {
            System.err.println("Error running test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
