package io.swagger.jaxrs2.integration;

import io.swagger.jaxrs2.Reader;
import io.swagger.oas.integration.GenericOpenApiContext;
import io.swagger.oas.integration.GenericOpenApiProcessor;
import io.swagger.oas.integration.OpenApiConfiguration;
import io.swagger.oas.integration.OpenApiContext;
import io.swagger.oas.integration.OpenApiProcessor;
import io.swagger.oas.models.OpenAPI;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class IntegrationTest {

    private final Set expectedKeys = new HashSet<String>(Arrays.asList("/packageA", "/packageB"));

    @Test(description = "scan a simple resource")
    public void shouldScanWithNewInitialization() {
        OpenApiConfiguration config = new OpenApiConfiguration()
                .resourcePackageNames("com.my.project.resources,org.my.project.resources")
                .openApi(new OpenAPI());
        OpenApiProcessor p = new GenericOpenApiProcessor().openApiConfiguration(config);


        p.setOpenApiReader(new Reader(config));
        p.setOpenApiScanner(new AnnotationJaxrsScanner().openApiConfiguration(config));
        OpenApiContext ctx = new GenericOpenApiContext().addOpenApiProcessor(p).init();
        // TODO basePath/url handling
        // TODO add getDefaultProcessor
        OpenAPI openApi = ctx.getDefaultProcessor().read();
        //OpenAPI openApi = ctx.getOpenApiProcessors().get("/").read();

        assertNotNull(openApi);
        assertEquals(openApi.getPaths().keySet(), expectedKeys);


        try {
            URL url = this.getClass().getResource("/integration/openapi-configuration.json");
            System.out.println(url.getPath());
            //if (true) return;
            String configPath = "/integration/openapi-configuration.json";
            //ctx = new XmlWebOpenApiContext().openApiConfiguration(config).init();
            //ctx = new XmlWebOpenApiContext().configLocation(url.getPath()).init();
            ctx = new XmlWebOpenApiContext().configLocation(configPath).init();
            openApi = ctx.read();

            assertNotNull(openApi);
            assertEquals(openApi.getPaths().keySet(), expectedKeys);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //assertEquals(openApi.getSchemes(), expectedSchemas);
    }

}
