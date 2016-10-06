package io.swagger.models;

import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class SwaggerTest {

    private Swagger swagger;
    private SecurityRequirement requirement;

    @BeforeMethod
    public void setup() {
        swagger = new Swagger();
        requirement = new SecurityRequirement();
    }

    @Test
    public void testAddScheme() {
        // when
        swagger.addScheme(Scheme.HTTP);

        // then
        assertTrue(swagger.getSchemes().contains(Scheme.HTTP),
                "The newly added scheme must be contained in the schemes list");
    }

    @Test
    public void testScheme() {
        // when
        swagger.scheme(Scheme.HTTPS);

        // then
        assertTrue(swagger.getSchemes().contains(Scheme.HTTPS),
                "The newly added scheme must be contained in the schemes list");
    }

    @Test
    public void testConsumes() {
        // when
        swagger.consumes("consumes");

        // then
        assertTrue(swagger.getConsumes().contains("consumes"),
                "The newly added consumes must be contained in the consumes list");
    }

    @Test
    public void testProduces() {
        // when
        swagger.produces("produces");

        // then
        assertTrue(swagger.getProduces().contains("produces"),
                "The newly added produces must be contained in the produces list");
    }

    @Test
    public void testSecurity() {
        // when
        swagger.security(requirement);

        // then
        assertTrue(swagger.getSecurity().contains(requirement),
                "The newly added requiement must be contained in the requiements list");
    }

    @Test
    public void testSetSecurityRequirement() {
        // when
        swagger.setSecurityRequirement(new ArrayList<SecurityRequirement>(Arrays.asList(requirement)));

        // then
        assertTrue(swagger.getSecurity().contains(requirement),
                "The newly added requiement must be contained in the requiement list");
    }

    @Test
    public void testAddSecurityDefinition() {
        // when
        swagger.addSecurityDefinition(requirement);

        // then
        assertTrue(swagger.getSecurity().contains(requirement),
                "The newly added requiement must be contained in the requiement list");
    }

    @Test
    public void testParameter() {
        // given
        Parameter parameter = Mockito.mock(Parameter.class);

        // when
        swagger.setParameters(null);
        String key = "key";

        // then
        assertNull(swagger.getParameter(key), "Cannot retrieve a key without adding it first");

        // when
        swagger.parameter(key, parameter);

        // then
        assertEquals(swagger.getParameters().get(key), parameter, "Must be able to retrieve the added key");
        assertEquals(swagger.getParameter(key), parameter, "Must be able to retrieave the added key");
    }

    @Test
    public void testResponse() {
        // given
        Response response = Mockito.mock(Response.class);

        // when
        swagger.response("44", response);

        // then
        assertEquals(swagger.getResponses().get("44"), response, "Must be able to retrieve the added response");
    }

    @Test
    public void testVendorExtension() {
        // given
        String vendorName = "x-vendor";
        String value = "value";

        // when
        swagger.vendorExtension(vendorName, value);
        swagger.vendorExtensions(new HashMap<String, Object>());

        // then
        assertEquals(swagger.getVendorExtensions().get(vendorName), value,
                "Must be able to retrieve the same value from the map");

        // when
        swagger.setVendorExtension(vendorName, value);

        // then
        assertEquals(swagger.getVendorExtensions().get(vendorName), value,
                "Must be able to retrieve the same value from the map");

        // when
        swagger.vendorExtensions(null);

        // then
        assertEquals(swagger.getVendorExtensions().get(vendorName), value,
                "Must be able to retrieve the same value from the map");

        // given
        swagger = new Swagger();

        // when
        swagger.vendorExtensions(new HashMap<String, Object>());

        // then
        assertNull(swagger.getVendorExtensions().get(vendorName), "Can not retrieve the key from an empty map");
    }

    @Test
    public void testTag() {
        // given
        Tag tag = new Tag();
        tag.setName("name");

        // when
        swagger.tag(tag);

        // then
        assertTrue(swagger.getTags().contains(tag), "The newly added tag must be contained in the tags list");

        // when
        swagger.tag(tag);

        // then
        assertTrue(swagger.getTags().contains(tag), "The newly added tag must be contained in the tags list");

        // when
        swagger.tags(Arrays.asList(tag));

        // then
        assertTrue(swagger.getTags().contains(tag), "The newly added tag must be contained in the tags list");
    }

    @Test
    public void testSetExternalDocs() {
        // given
        ExternalDocs externalDocs = new ExternalDocs();

        // when
        swagger.setExternalDocs(externalDocs);

        // then
        assertEquals(swagger.getExternalDocs(), externalDocs, "The get externalDocs must be the same as the set one");
    }

    @Test
    public void testPath() {
        // when
        String key = "key";
        swagger.setPaths(null);

        // then
        assertNull(swagger.getPaths(), "If paths is set to null, then it must resolve to null");
        assertNull(swagger.getPath(key), "If paths is set to null, then you cannot retrieve any key from it");

        // given
        Path path = new Path();

        // when
        swagger.path(key, path);

        // then
        assertEquals(swagger.getPaths().get(key), path, "Must be able to retrieve the path");
        assertEquals(swagger.getPath(key), path, "Must be able to retrieve the path");
    }

    @Test
    public void testSecurityDefinition() {
        // given
        SecuritySchemeDefinition securityDefinition = new BasicAuthDefinition();
        String name = "name";

        // when
        swagger.securityDefinition(name, securityDefinition);

        // then
        assertEquals(swagger.getSecurityDefinitions().get(name), securityDefinition,
                "Must be able to retrieve the added security definition");
    }

    @Test
    public void testModel() {
        // given
        Model model = new ComposedModel();
        String name = "name";

        // when
        swagger.model(name, model);

        // then
        assertEquals(swagger.getDefinitions().get(name), model, "Must be able to retrieve the added model");
    }
}
