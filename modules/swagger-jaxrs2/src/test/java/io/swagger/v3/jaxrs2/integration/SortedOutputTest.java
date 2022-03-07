package io.swagger.v3.jaxrs2.integration;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.PathsSerializer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.ObjectMapperProcessor;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class SortedOutputTest {
    private JaxrsApplicationAndAnnotationScanner scanner;

    @Path("/app")
    protected static class ResourceInApplication {
        @Operation(operationId = "test.")
        @GET
        public void getTest(@Parameter(name = "test") ArrayList<String> tenantId) {
            return;
        }
    }

    @BeforeMethod
    public void setUp() {
        scanner = new JaxrsApplicationAndAnnotationScanner();

        scanner.setApplication(new Application() {
            @Override
            public Set<Class<?>> getClasses() {
                return Collections.singleton(ResourceInApplication.class);
            }
        });
    }


    @Test(description = "sort output test")
    public void sortOutputTest() throws Exception {


        SwaggerConfiguration openApiConfiguration = new SwaggerConfiguration()
                .sortOutput(true)
                .resourcePackages(Collections.singleton("com.my.sorted.resources"));

        OpenApiContext ctx = new JaxrsOpenApiContext<>()
                .openApiConfiguration(openApiConfiguration)
                .init();

        OpenAPI openApi = ctx.read();
        String sorted = ctx.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(openApi);

        openApiConfiguration = new SwaggerConfiguration()
                .resourcePackages(Collections.singleton("com.my.sorted.resources"));

        ctx = new JaxrsOpenApiContext<>()
                .openApiConfiguration(openApiConfiguration)
                .init();

        String notSorted = ctx.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(openApi);

        assertEquals(sorted, expectedSorted);
        assertEquals(notSorted, expectedNotSorted);

    }

    @JsonPropertyOrder(value = {"openapi", "info", "externalDocs", "servers", "security", "tags", "paths", "components"}, alphabetic = true)
    public static abstract class SortedOpenAPIMixin {

        @JsonAnyGetter
        @JsonPropertyOrder(alphabetic = true)
        public abstract Map<String, Object> getExtensions();

        @JsonAnySetter
        public abstract void addExtension(String name, Object value);

        @JsonSerialize(using = PathsSerializer.class)
        public abstract Paths getPaths();
    }

    @JsonPropertyOrder(value = {"type", "format"}, alphabetic = true)
    public static abstract class SortedSchemaMixin {

        @JsonAnyGetter
        @JsonPropertyOrder(alphabetic = true)
        public abstract Map<String, Object> getExtensions();

        @JsonAnySetter
        public abstract void addExtension(String name, Object value);

        @JsonIgnore
        public abstract boolean getExampleSetFlag();

        @JsonInclude(JsonInclude.Include.CUSTOM)
        public abstract Object getExample();

        @JsonIgnore
        public abstract Map<String, Object> getJsonSchema();

        @JsonIgnore
        public abstract BigDecimal getExclusiveMinimumValue();

        @JsonIgnore
        public abstract BigDecimal getExclusiveMaximumValue();

        @JsonIgnore
        public abstract Map<String, Schema> getPatternProperties();

        @JsonIgnore
        public abstract Schema getContains();
        @JsonIgnore
        public abstract String get$id();
        @JsonIgnore
        public abstract String get$anchor();
        @JsonIgnore
        public abstract String get$schema();
        @JsonIgnore
        public abstract Set<String> getTypes();

        @JsonIgnore
        public abstract Object getJsonSchemaImpl();


    }

    public static class SortedProcessor implements ObjectMapperProcessor {

        @Override
        public void processOutputJsonObjectMapper(ObjectMapper mapper) {
            mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
            mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
            mapper.addMixIn(OpenAPI.class, SortedOpenAPIMixin.class);
            mapper.addMixIn(Schema.class, SortedSchemaMixin.class);
        }

        @Override
        public void processOutputYamlObjectMapper(ObjectMapper mapper) {
            mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
            mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
            mapper.addMixIn(OpenAPI.class, SortedOpenAPIMixin.class);
            mapper.addMixIn(Schema.class, SortedSchemaMixin.class);
        }
    }

    @Test(description = "config output test")
    public void configOutputTest() throws Exception {
        SwaggerConfiguration openApiConfiguration = new SwaggerConfiguration()
                .objectMapperProcessorClass(SortedProcessor.class.getName())
                .resourcePackages(Collections.singleton("com.my.sorted.resources"));

        OpenApiContext ctx = new JaxrsOpenApiContext<>()
                .openApiConfiguration(openApiConfiguration)
                .init();

        OpenAPI openApi = ctx.read();
        String sorted = ctx.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(openApi);

        openApiConfiguration = new SwaggerConfiguration()
                .resourcePackages(Collections.singleton("com.my.sorted.resources"));

        ctx = new JaxrsOpenApiContext<>()
                .openApiConfiguration(openApiConfiguration)
                .init();

        String notSorted = ctx.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(openApi);

        assertEquals(sorted, expectedSorted);
        assertEquals(notSorted, expectedNotSorted);

    }
    String expectedSorted = "openapi: 3.0.1\n" +
            "paths:\n" +
            "  /sorted/pet:\n" +
            "    get:\n" +
            "      operationId: foo\n" +
            "      responses:\n" +
            "        default:\n" +
            "          content:\n" +
            "            '*/*':\n" +
            "              schema:\n" +
            "                $ref: '#/components/schemas/Pet'\n" +
            "          description: default response\n" +
            "components:\n" +
            "  schemas:\n" +
            "    Category:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        id:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "        name:\n" +
            "          type: string\n" +
            "      xml:\n" +
            "        name: Category\n" +
            "    Pet:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        category:\n" +
            "          $ref: '#/components/schemas/Category'\n" +
            "        id:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "        name:\n" +
            "          type: string\n" +
            "        photoUrls:\n" +
            "          type: array\n" +
            "          items:\n" +
            "            type: string\n" +
            "            xml:\n" +
            "              name: photoUrl\n" +
            "          xml:\n" +
            "            wrapped: true\n" +
            "        status:\n" +
            "          type: string\n" +
            "          description: pet status in the store\n" +
            "          enum:\n" +
            "          - \"available,pending,sold\"\n" +
            "        tags:\n" +
            "          type: array\n" +
            "          items:\n" +
            "            $ref: '#/components/schemas/Tag'\n" +
            "          xml:\n" +
            "            wrapped: true\n" +
            "      xml:\n" +
            "        name: Pet\n" +
            "    Tag:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        id:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "        name:\n" +
            "          type: string\n" +
            "      xml:\n" +
            "        name: Tag\n";

    String expectedNotSorted = "openapi: 3.0.1\n" +
            "paths:\n" +
            "  /sorted/pet:\n" +
            "    get:\n" +
            "      operationId: foo\n" +
            "      responses:\n" +
            "        default:\n" +
            "          description: default response\n" +
            "          content:\n" +
            "            '*/*':\n" +
            "              schema:\n" +
            "                $ref: '#/components/schemas/Pet'\n" +
            "components:\n" +
            "  schemas:\n" +
            "    Category:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        id:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "        name:\n" +
            "          type: string\n" +
            "      xml:\n" +
            "        name: Category\n" +
            "    Pet:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        id:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "        category:\n" +
            "          $ref: '#/components/schemas/Category'\n" +
            "        name:\n" +
            "          type: string\n" +
            "        photoUrls:\n" +
            "          type: array\n" +
            "          xml:\n" +
            "            wrapped: true\n" +
            "          items:\n" +
            "            type: string\n" +
            "            xml:\n" +
            "              name: photoUrl\n" +
            "        tags:\n" +
            "          type: array\n" +
            "          xml:\n" +
            "            wrapped: true\n" +
            "          items:\n" +
            "            $ref: '#/components/schemas/Tag'\n" +
            "        status:\n" +
            "          type: string\n" +
            "          description: pet status in the store\n" +
            "          enum:\n" +
            "          - \"available,pending,sold\"\n" +
            "      xml:\n" +
            "        name: Pet\n" +
            "    Tag:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        id:\n" +
            "          type: integer\n" +
            "          format: int64\n" +
            "        name:\n" +
            "          type: string\n" +
            "      xml:\n" +
            "        name: Tag\n";

}
