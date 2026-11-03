package io.swagger.v3.oas.models;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class OpenAPISpecVersionSyncTest {

    @Test
    public void fromOpenapiMapsKnownVersions() {
        assertEquals(SpecVersion.fromOpenapi("3.0.0"), SpecVersion.V30);
        assertEquals(SpecVersion.fromOpenapi("3.0.1"), SpecVersion.V30);
        assertEquals(SpecVersion.fromOpenapi("3.0.4"), SpecVersion.V30);
        assertEquals(SpecVersion.fromOpenapi("3.1.0"), SpecVersion.V31);
        assertEquals(SpecVersion.fromOpenapi("3.1.1"), SpecVersion.V31);
    }

    @Test
    public void fromOpenapiReturnsNullForUnknownOrMissing() {
        assertNull(SpecVersion.fromOpenapi(null));
        assertNull(SpecVersion.fromOpenapi(""));
        assertNull(SpecVersion.fromOpenapi("4.0.0"));
        assertNull(SpecVersion.fromOpenapi("3.x"));
        assertNull(SpecVersion.fromOpenapi("abc"));
        assertNull(SpecVersion.fromOpenapi("3.0"));
        assertNull(SpecVersion.fromOpenapi("3.10.0"));
    }

    @Test
    public void setOpenapiSyncsSpecVersion() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setOpenapi("3.1.0");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V31);

        openAPI.setOpenapi("3.0.4");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V30);
    }

    @Test
    public void openapiBuilderSyncsSpecVersion() {
        assertEquals(new OpenAPI().openapi("3.1.0").getSpecVersion(), SpecVersion.V31);
        assertEquals(new OpenAPI().openapi("3.0.2").getSpecVersion(), SpecVersion.V30);
    }

    @Test
    public void unknownOpenapiStringLeavesSpecVersion() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setOpenapi("4.0.0");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V30);
        openAPI.setOpenapi(null);
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V30);
        openAPI.setOpenapi("");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V30);
    }

    @Test
    public void defaultInstanceIsConsistent() {
        OpenAPI openAPI = new OpenAPI();
        assertEquals(openAPI.getOpenapi(), "3.0.1");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V30);
    }

    @Test
    public void setSpecVersionNeverRewritesOpenapiString() {
        // tagging an instance (e.g. by a deserializer) must not rewrite the
        // declared version string; setSpecVersion stays a low-level tag
        OpenAPI openAPI = new OpenAPI().openapi("3.0.5");
        openAPI.setSpecVersion(SpecVersion.V31);
        assertEquals(openAPI.getOpenapi(), "3.0.5");
        assertEquals(openAPI.getSpecVersion(), SpecVersion.V31);
    }
}
