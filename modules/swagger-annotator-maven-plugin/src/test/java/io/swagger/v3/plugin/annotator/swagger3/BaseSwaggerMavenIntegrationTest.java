package io.swagger.v3.plugin.annotator.swagger3;

import io.swagger.v3.plugin.annotator.Swagger3AnnotatorMojo;
import java.io.File;

public abstract class BaseSwaggerMavenIntegrationTest extends BetterAbstractMojoTestCase {

    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    Swagger3AnnotatorMojo runTest(File pom) throws Exception {
        assertNotNull(pom);
        assertTrue(pom.exists());

        Swagger3AnnotatorMojo swaggerMojo = (Swagger3AnnotatorMojo) lookupConfiguredMojo(pom, "swagger3-annotator");
        assertNotNull(swaggerMojo);

        swaggerMojo.execute();
        return swaggerMojo;
    }
}
