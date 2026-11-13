package io.swagger.v3.jaxrs2.annotations.parameters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ParameterPlaceholderTest extends AbstractAnnotationTest {

    @DataProvider
    public Object[][] formats() {
        return new Object[][]{
                {false, Json.mapper()},
                {false, Yaml.mapper()},
                {true, Json31.mapper()},
                {true, Yaml31.mapper()}
        };
    }

    @Test(dataProvider = "formats")
    public void serializePlaceholderAsText(boolean openapi31, ObjectMapper mapper) throws IOException {
        JsonNode parameters = readParameters(openapi31, mapper, "/clients");
        JsonNode parameter = parameters.get(0);

        assertEquals(parameters.size(), 1);
        assertEquals(parameter.get("name").asText(), "clientId");
        assertEquals(parameter.get("in").asText(), "query");
        assertTrue(parameter.get("x-placeholder").isTextual());
        assertEquals(parameter.get("x-placeholder").asText(), "1");
        assertFalse(parameter.has("placeholder"));
        assertFalse(parameter.has("example"));
        assertFalse(parameter.path("schema").has("default"));
        assertFalse(parameter.path("required").asBoolean());
    }

    @Test(dataProvider = "formats")
    public void preserveExistingMetadata(boolean openapi31, ObjectMapper mapper) throws IOException {
        JsonNode parameters = readParameters(openapi31, mapper, "/metadata");

        assertEquals(parameters.size(), 3);
        JsonNode withExample = parameters.get(0);
        assertEquals(withExample.get("x-placeholder").asText(), "Enter a client code");
        assertEquals(withExample.get("example").asInt(), 2);
        assertEquals(withExample.path("schema").get("default").asInt(), 3);
        assertEquals(withExample.get("description").asText(), "Client code");
        assertTrue(withExample.get("required").asBoolean());
        assertEquals(withExample.get("x-other").asText(), "kept");

        assertEquals(parameters.get(1).get("x-placeholder").asText(), "Explicit hint");
        assertEquals(parameters.get(2).get("x-placeholder").asText(), "Extension only");
    }

    @Test(dataProvider = "formats")
    public void ignoreBlankHintsAndPreserveLiteralText(boolean openapi31, ObjectMapper mapper) throws IOException {
        JsonNode parameters = readParameters(openapi31, mapper, "/hints");

        assertEquals(parameters.size(), 5);
        for (int i = 0; i < 3; i++) {
            assertFalse(parameters.get(i).has("x-placeholder"));
        }
        assertTrue(parameters.get(3).get("x-placeholder").isTextual());
        assertEquals(parameters.get(3).get("x-placeholder").asText(), "true");
        assertEquals(parameters.get(4).get("x-placeholder").asText(), "  yyyy-mm-dd  ");
    }

    @Test(dataProvider = "formats")
    public void respectHiddenAndReferencedParameters(boolean openapi31, ObjectMapper mapper) throws IOException {
        JsonNode parameters = readParameters(openapi31, mapper, "/references");

        assertEquals(parameters.size(), 1);
        assertEquals(parameters.get(0).get("$ref").asText(), "#/components/parameters/clientId");
        assertFalse(parameters.get(0).has("x-placeholder"));
    }

    private JsonNode readParameters(boolean openapi31, ObjectMapper mapper, String path) throws IOException {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).openAPI31(openapi31));
        OpenAPI openAPI = reader.read(PlaceholderResource.class);
        JsonNode document = mapper.readTree(mapper.writeValueAsString(openAPI));
        return document.path("paths").path(path).path("get").path("parameters");
    }

    static class PlaceholderResource {
        @GET
        @Path("/clients")
        public void clients(@QueryParam("clientId") @Parameter(placeholder = "1") Integer clientId) {
        }

        @GET
        @Path("/metadata")
        @Operation(parameters = {
                @Parameter(name = "withExample", in = ParameterIn.QUERY, description = "Client code",
                        required = true, placeholder = "Enter a client code", example = "2",
                        schema = @Schema(type = "integer", defaultValue = "3"),
                        extensions = @Extension(properties = @ExtensionProperty(name = "other", value = "kept"))),
                @Parameter(name = "explicit", in = ParameterIn.QUERY, placeholder = "Annotation hint",
                        extensions = @Extension(properties = @ExtensionProperty(name = "x-placeholder", value = "Explicit hint"))),
                @Parameter(name = "extensionOnly", in = ParameterIn.QUERY,
                        extensions = @Extension(properties = @ExtensionProperty(name = "x-placeholder", value = "Extension only")))
        })
        public void metadata() {
        }

        @GET
        @Path("/hints")
        @Operation(parameters = {
                @Parameter(name = "omitted", in = ParameterIn.QUERY),
                @Parameter(name = "empty", in = ParameterIn.QUERY, placeholder = ""),
                @Parameter(name = "blank", in = ParameterIn.QUERY, placeholder = " \t "),
                @Parameter(name = "booleanText", in = ParameterIn.QUERY, placeholder = "true"),
                @Parameter(name = "padded", in = ParameterIn.QUERY, placeholder = "  yyyy-mm-dd  ")
        })
        public void hints() {
        }

        @GET
        @Path("/references")
        @Operation(parameters = {
                @Parameter(name = "hidden", in = ParameterIn.QUERY, hidden = true, placeholder = "Hidden hint"),
                @Parameter(ref = "#/components/parameters/clientId", placeholder = "Reference hint")
        })
        public void references() {
        }
    }
}
