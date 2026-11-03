package io.swagger.v3.core.deserialization;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class OpenAPIVersionSyncDeserializationTest {

    private static final String OAS31_JSON = "{\"openapi\":\"3.1.0\",\"info\":{\"title\":\"t\",\"version\":\"1\"}}";
    private static final String OAS31_YAML = "openapi: 3.1.0\ninfo:\n  title: t\n  version: \"1\"\n";

    @Test
    public void readerForUpdatingSyncsSpecVersionJson31() throws IOException {
        OpenAPI openAPI = Json31.mapper().readerForUpdating(new OpenAPI()).readValue(OAS31_JSON);
        assertEquals(openAPI.getOpenapi(), "3.1.0");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V31);
    }

    @Test
    public void readerForUpdatingSyncsSpecVersionJson() throws IOException {
        OpenAPI openAPI = Json.mapper().readerForUpdating(new OpenAPI()).readValue(OAS31_JSON);
        assertEquals(openAPI.getOpenapi(), "3.1.0");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V31);
    }

    @Test
    public void readerForUpdatingSyncsSpecVersionYaml31() throws IOException {
        OpenAPI openAPI = Yaml31.mapper().readerForUpdating(new OpenAPI()).readValue(OAS31_YAML);
        assertEquals(openAPI.getOpenapi(), "3.1.0");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V31);
    }

    @Test
    public void regularReadTagsSpecVersion() throws IOException {
        assertEquals(Json31.mapper().readValue(OAS31_JSON, OpenAPI.class).getSpecVersion(), SpecVersion.V31);
        assertEquals(Yaml31.mapper().readValue(OAS31_YAML, OpenAPI.class).getSpecVersion(), SpecVersion.V31);
        assertEquals(Json.mapper().readValue(OAS31_JSON, OpenAPI.class).getSpecVersion(), SpecVersion.V31);
    }

    @Test
    public void deserializerTaggingPreservesDeclaredString() throws IOException {
        // a 3.0.x document read through the 3.1 mapper is tagged V31 by
        // OpenAPI31Deserializer; the declared version string is preserved
        final String doc = "{\"openapi\":\"3.0.5\",\"info\":{\"title\":\"t\",\"version\":\"1\"}}";
        OpenAPI openAPI = Json31.mapper().readValue(doc, OpenAPI.class);
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V31);
        assertEquals(openAPI.getOpenapi(), "3.0.5");

        // readerForUpdating bypasses OpenAPI31Deserializer, so no V31 tag is
        // applied; the model stays consistent with the document's own string
        openAPI = Json31.mapper().readerForUpdating(new OpenAPI()).readValue(doc);
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V30);
        assertEquals(openAPI.getOpenapi(), "3.0.5");
    }

    @Test
    public void spec30DocumentStaysV30() throws IOException {
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore-3.0.yaml");
        OpenAPI openAPI = Yaml.mapper().readValue(yaml, OpenAPI.class);
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V30);

        openAPI = Json.mapper().readerForUpdating(new OpenAPI()).readValue("{\"openapi\":\"3.0.3\"}");
        assertEquals(openAPI.getOpenapi(), "3.0.3");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V30);
    }
}
