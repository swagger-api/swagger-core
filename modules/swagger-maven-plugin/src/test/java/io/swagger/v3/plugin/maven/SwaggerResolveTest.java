package io.swagger.v3.plugin.maven;

import io.swagger.v3.oas.models.OpenAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SwaggerResolveTest extends ASwaggerMavenIntegrationTest {

    protected static Logger LOGGER = LoggerFactory.getLogger(SwaggerResolveTest.class);

    public void testResolve() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFile.xml");
        checkOutput(runTest(pom));
    }

    public void testResolve31() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFile31.xml");
        checkOutput(runTest(pom, this::validateOpenApi31Content));
    }

    void validateOpenApi31Content(OpenAPI openAPI) {
        assertEquals(
                Boolean.TRUE,
                openAPI
                        .getPaths()
                        .get("/pet31")
                        .getPut()
                        .getResponses()
                        .get("200")
                        .getContent()
                        .get("application/xml")
                        .getSchema()
                        .getReadOnly());
    }

    public void testResolveWithFilter() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileWithFilter.xml");
        checkOutput(runTest(pom));
    }

    public void testResolveNoName() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileNoName.xml");
        checkOutput(runTest(pom));
    }

    public void testResolveJsonAndYaml() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileJsonAndYaml.xml");
        checkOutput(runTest(pom));
    }

    public void testResolveWithJsonInput() throws Exception {
        File pom = getTestFile("src/test/resources/pom.resolveToFileFromJsonInput.xml");
        checkOutput(runTest(pom));
    }

    private void checkOutput(SwaggerMojo mojo) {
        assertNull(mojo.getConfigurationFilePath());
    }
}
