package io.swagger.v3.plugin.maven;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.plexus.configuration.PlexusConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public abstract class ASwaggerMavenIntegrationTest extends BetterAbstractMojoTestCase {

    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    SwaggerMojo runTest(File pom) throws Exception {
        return runTest(pom, this::validateOpenApiContent);
    }

    SwaggerMojo runTest(File pom, Consumer<OpenAPI> validator) throws Exception {
        assertNotNull(pom);
        assertTrue(pom.exists());

        SwaggerMojo swaggerMojo = (SwaggerMojo) lookupConfiguredMojo(pom, "resolve");
        assertNotNull(swaggerMojo);
        // set random context id to not mix states with multiple tests
        swaggerMojo.setContextId(RandomStringUtils.randomAscii(32));
        // set ClassRealm
        swaggerMojo.getPluginDescriptor().setClassRealm(getContainer().createChildRealm(RandomStringUtils.randomAscii(32)));

        swaggerMojo.execute();

        final PlexusConfiguration config = extractPluginConfiguration("swagger-maven-plugin", pom);
        String outputPath = swaggerMojo.getOutputPath();
        String outputFile = config.getChild("outputFileName").getValue();
        if (outputFile == null) {
            outputFile = "openapi";
        }
        String format = config.getChild("outputFormat").getValue();
        if (format.toLowerCase().equals("yaml") || format.toLowerCase().equals("jsonandyaml")) {
            Path path = Paths.get(outputPath, outputFile + ".yaml");
            File file = path.toFile();
            assertTrue(Files.isRegularFile(path));
            String content = FileUtils.readFileToString(file, "UTF-8");
            final OpenAPI openAPI = Yaml.mapper().readValue(content, OpenAPI.class);
            assertNotNull(openAPI);
            validator.accept(openAPI);
        }
        if (format.toLowerCase().equals("json") || format.toLowerCase().equals("jsonandyaml")) {
            Path path = Paths.get(outputPath, outputFile + ".json");
            File file = path.toFile();
            assertTrue(Files.isRegularFile(path));
            String content = FileUtils.readFileToString(file, "UTF-8");
            final OpenAPI openAPI = Json.mapper().readValue(content, OpenAPI.class);
            assertNotNull(openAPI);
            validator.accept(openAPI);
        }
        return swaggerMojo;
    }

    void validateOpenApiContent(OpenAPI openAPI) {
        assertEquals(2, openAPI.getServers().get(0).getVariables().size());
        assertNotNull(openAPI.getInfo());
    }
}
