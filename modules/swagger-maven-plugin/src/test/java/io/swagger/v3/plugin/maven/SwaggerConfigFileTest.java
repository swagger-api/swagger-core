package io.swagger.v3.plugin.maven;

import io.swagger.v3.oas.models.media.Schema;

import java.io.File;
import java.math.BigDecimal;

public class SwaggerConfigFileTest extends ASwaggerMavenIntegrationTest {


    public void testResolveFromConfigIncludingOpenApi() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileFromConfigAndOpenApi.xml");

        SwaggerMojo mojo = runTest(pom, openAPI -> {
            assertEquals(1, openAPI.getServers().get(0).getVariables().size());
            assertNotNull(openAPI.getInfo());
        });
        assertTrue(mojo.getInternalConfiguration().isPrettyPrint());
        assertEquals(1, mojo.getInternalConfiguration().getResourcePackages().size());
        assertEquals("io.swagger.v3.plugin.maven.petstore.petstore", mojo.getInternalConfiguration().getResourcePackages().iterator().next());
        assertEquals(1, mojo.getInternalConfiguration().getOpenAPI().getServers().get(0).getVariables().size());
    }

    public void testResolveFromConfig() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileFromConfig.xml");

        SwaggerMojo mojo = runTest(pom);
        assertTrue(mojo.getInternalConfiguration().isPrettyPrint());
        assertEquals(1, mojo.getInternalConfiguration().getResourcePackages().size());
        assertEquals("io.swagger.v3.plugin.maven.petstore.petstore", mojo.getInternalConfiguration().getResourcePackages().iterator().next());
        assertEquals(2, mojo.getInternalConfiguration().getOpenAPI().getServers().get(0).getVariables().size());
    }

    public void testResolveFromConfigWithOpenAPI31Option() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileFromConfigWithOAS3.1Filter.xml");
        SwaggerMojo mojo = runTest(pom, openAPI -> {
            assertEquals("3.1.0", openAPI.getOpenapi());
            assertNotNull(openAPI.getInfo());
            assertNotNull(openAPI.getComponents());
            assertNotNull(openAPI.getComponents().getSchemas());
            assertNotNull(openAPI.getComponents().getSchemas().get("Address"));
            assertNotNull(openAPI.getComponents().getSchemas().get("Address").getProperties());

            Schema codeProperty = (Schema) openAPI.getComponents().getSchemas().get("Address").getProperties().get("code");

            assertNotNull(codeProperty);
            assertNotNull(codeProperty.getMaximum());
            assertEquals(new BigDecimal(50), codeProperty.getMaximum());
            assertNotNull(codeProperty.getExclusiveMaximumValue());
            assertEquals(new BigDecimal(50), codeProperty.getExclusiveMaximumValue());
        });
        assertTrue(mojo.getInternalConfiguration().isPrettyPrint());
        assertTrue(mojo.getInternalConfiguration().isOpenAPI31());
    }
}
