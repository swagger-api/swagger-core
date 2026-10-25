package io.swagger.v3.jpms.test;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.integration.ServiceOpenApiConfigurationLoader;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfigBuilder;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.rest.Reader;
import io.swagger.v3.rest.ext.AbstractOpenAPIExtension;
import io.swagger.v3.rest.ext.OpenAPIExtensions;
import io.swagger.v3.rest.integration.RestOpenApiContextBuilder;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import tools.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/** Runs in a separate JVM with only packaged JARs on the module path. */
public final class ModulePathSmoke {
    public static void main(String[] args) throws Exception {
        for (String name : new String[] {"io.swagger.v3.oas.annotations", "io.swagger.v3.oas.models",
                "io.swagger.v3.core", "io.swagger.v3.oas.integration", "io.swagger.v3.rest",
                "io.swagger.v3.rest.integration.servlet"}) {
            Module module = ModuleLayer.boot().findModule(name).orElseThrow();
            check(!module.getDescriptor().isAutomatic(), name + " must have an explicit descriptor");
        }

        check(ModelConverters.getInstance(true).getConverters().stream().anyMatch(ProbeConverter.class::isInstance),
                "ModelConverter service loading");
        check(new ServiceOpenApiConfigurationLoader().load("service").isOpenAPI31(),
                "OpenAPIConfigBuilder service loading");
        check(OpenAPIExtensions.getExtensions().stream().anyMatch(ProbeExtension.class::isInstance),
                "OpenAPIExtension service loading");
        check(ServiceLoader.load(ServletContainerInitializer.class).stream().anyMatch(provider ->
                        provider.get().getClass().getName().equals(
                                "io.swagger.v3.rest.integration.servlet.SwaggerServletInitializer")),
                "ServletContainerInitializer service loading");

        SwaggerConfiguration configuration = new SwaggerConfiguration().openAPI31(true).readAllResources(true);
        OpenAPI api = new Reader(configuration).read(Resource.class);
        for (ObjectMapper mapper : new ObjectMapper[] {Json31.mapper(), Yaml31.mapper()}) {
            assertRoundTrip(api, mapper, Json31.mapper());
        }
        OpenAPI api30 = new Reader(new SwaggerConfiguration().readAllResources(true)).read(Resource.class);
        for (ObjectMapper mapper : new ObjectMapper[] {Json.mapper(), Yaml.mapper()}) {
            assertRoundTrip(api30, mapper, Json.mapper());
        }

        OpenAPI contextApi = new RestOpenApiContextBuilder<>()
                .ctxId("jpms-smoke")
                .openApiConfiguration(new SwaggerConfiguration().openAPI31(true)
                        .resourceClasses(java.util.Set.of(Resource.class.getName())))
                .buildContext(true).read();
        check(contextApi.getPaths().containsKey("/jpms"), "REST context and resource scanner");
        OpenAPI scannedApi = new RestOpenApiContextBuilder<>()
                .ctxId("jpms-package-scan")
                .openApiConfiguration(new SwaggerConfiguration().openAPI31(true)
                        .resourcePackages(java.util.Set.of(Resource.class.getPackageName())))
                .buildContext(true).read();
        check(scannedApi.getPaths().containsKey("/jpms"), "ClassGraph package scanning on the module path");
        System.out.println("JPMS smoke passed: six descriptors, four service contracts, REST generation, JSON/YAML 3.0/3.1");
    }

    private static void assertRoundTrip(OpenAPI api, ObjectMapper mapper, ObjectMapper jsonMapper) {
        OpenAPI decoded = mapper.readValue(mapper.writeValueAsString(api), OpenAPI.class);
        var tree = jsonMapper.readTree(jsonMapper.writeValueAsString(decoded));
        check("#/components/schemas/MapResponse".equals(tree.at(
                "/paths/~1jpms/get/responses/200/content/application~1json/schema/$ref").asText()),
                "response reference round trip");
        check("#/components/schemas/MapValue".equals(tree.at(
                "/components/schemas/MapResponse/properties/entries/additionalProperties/$ref").asText()),
                "map value reference round trip");
        check("string".equals(tree.at("/components/schemas/MapValue/properties/value/type").asText()),
                "model property round trip");
    }

    private static void check(boolean condition, String contract) {
        if (!condition) {
            throw new AssertionError(contract);
        }
    }

    public static class ProbeConverter implements ModelConverter {
        @Override
        public io.swagger.v3.oas.models.media.Schema resolve(AnnotatedType type,
                ModelConverterContext context, Iterator<ModelConverter> chain) {
            return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
        }
    }

    public static class ProbeConfigBuilder implements OpenAPIConfigBuilder {
        @Override
        public OpenAPIConfiguration build() {
            return new SwaggerConfiguration().openAPI31(true);
        }
    }

    public static class ProbeExtension extends AbstractOpenAPIExtension {
    }

    @Path("/jpms")
    public static class Resource {
        @GET
        @Produces("application/json")
        @ApiResponse(responseCode = "200", description = "Typed map",
                content = @Content(schema = @Schema(implementation = MapResponse.class)))
        public MapResponse get() {
            return new MapResponse();
        }
    }

    @Schema(name = "MapResponse")
    public static class MapResponse {
        public Map<String, MapValue> entries;
    }

    @Schema(name = "MapValue")
    public static class MapValue {
        public String value;
    }
}
