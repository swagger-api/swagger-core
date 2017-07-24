package io.swagger.oas.integration;

import io.swagger.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

// TODO
public class IntegrationTest {

    private final Set expectedKeys = new HashSet<String>(Arrays.asList("/packageA", "/packageB"));

    @Test(description = "scan a simple resource")
    public void shouldScanWithNewInitialization() throws Exception{
        OpenApiConfiguration config = new OpenApiConfiguration()
                .withResourcePackage("com.my.project.resources,org.my.project.resources")
                .openApi(new OpenAPI());
        OpenApiProcessor p = new GenericOpenApiProcessor().withOpenApiConfiguration(config);


        p.setOpenApiReader(new GenericOpenApiContext().buildReader(config));
        p.setOpenApiScanner(new GenericOpenApiContext().buildScanner(config));
        OpenApiContext ctx = new GenericOpenApiContext().addOpenApiProcessor(p).init();
        // TODO basePath/url handling
        // TODO add getDefaultProcessor
        OpenAPI openApi = ctx.getDefaultProcessor().read();
        //OpenAPI openApi = ctx.getOpenApiProcessors().get("/").read();

        assertNotNull(openApi);
        // TODO
        //assertEquals(openApi.getPaths().keySet(), expectedKeys);

        //assertEquals(openApi.getSchemes(), expectedSchemas);
    }

}
