package io.swagger.v3.oas.integration;

import io.swagger.v3.oas.integration.api.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.testng.annotations.Test;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

public class IntegrationTest {

    @Test(description = "initialize a context and read")
    public void shouldInitialize() throws Exception {

        OpenAPIConfiguration config = new SwaggerConfiguration()
                .resourcePackages(Stream.of("com.my.project.resources", "org.my.project.resources").collect(Collectors.toSet()))
                .openAPI(new OpenAPI().info(new Info().description("TEST INFO DESC")));

        OpenApiContext ctx = new GenericOpenApiContext()
                .openApiConfiguration(config)
                .init();
        OpenAPI openApi = ctx.read();

        assertNotNull(openApi);
    }

    @Test(description = "processJsonObjectMapper default returns same mapper")
    public void testProcessJsonObjectMapperDefaultReturnsSameMapper() {
        ObjectMapperProcessor processor = new ObjectMapperProcessor() {
        };
        ObjectMapper input = new ObjectMapper();
        ObjectMapper result = processor.processJsonObjectMapper(input);
        assertSame(result, input);
    }

    @Test(description = "processJsonObjectMapper custom impl can rebuild mapper with different config")
    public void testProcessJsonObjectMapperCustomImplReturnValueHonored() {
        ObjectMapperProcessor processor = new ObjectMapperProcessor() {
            @Override
            public ObjectMapper processJsonObjectMapper(ObjectMapper mapper) {
                return mapper.rebuild()
                        .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                        .build();
            }
        };
        ObjectMapper input = new ObjectMapper();
        ObjectMapper result = processor.processJsonObjectMapper(input);
        assertNotSame(result, input, "rebuild produces new instance");
        assertTrue(result.isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY),
                "returned mapper must carry the custom config");
    }

}
