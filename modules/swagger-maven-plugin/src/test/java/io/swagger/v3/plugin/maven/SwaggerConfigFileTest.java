package io.swagger.v3.plugin.maven;

import java.io.File;

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
}
