package io.swagger.jaxrs2.integration;

import io.swagger.jaxrs2.Reader;
import io.swagger.oas.integration.GenericOpenApiContext;
import io.swagger.oas.integration.OpenApiConfiguration;
import io.swagger.oas.integration.OpenApiContext;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.info.Info;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

public class IntegrationTest {

    private final Set expectedKeys = new HashSet<String>(Arrays.asList("/packageA", "/packageB"));

    @Test(description = "scan a simple resource")
    public void shouldScanWithNewInitialization() throws Exception{
        OpenApiConfiguration config = new OpenApiConfiguration()
                .resourcePackages(Stream.of("com.my.project.resources", "org.my.project.resources").collect(Collectors.toSet()))
                .openApi(new OpenAPI().info(new Info().description("TEST INFO DESC")));
        OpenApiContext ctx = new GenericOpenApiContext()
                .openApiConfiguration(config)
                .openApiReader(new Reader(config))
                .openApiScanner(new JaxrsApplicationAndAnnotationScanner().openApiConfiguration(config))
                .init();

        OpenAPI openApi = ctx.read();

        assertNotNull(openApi);
        assertEquals(openApi.getPaths().keySet(), expectedKeys);

        try {
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
    }

}
