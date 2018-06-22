package io.swagger.models;

import io.swagger.TestUtils;
import io.swagger.models.parameters.Parameter;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class OperationTest {

    private Operation operation;

    @BeforeMethod
    public void setup() {
        operation = new Operation();
    }

    @Test
    public void testBuilders() throws Exception {
        TestUtils.testBuilders(Operation.class, new HashSet<String>(Arrays.asList("deprecated", "vendorExtensions")));
    }

    @Test
    public void testAddScheme() {
        // when
        operation.addScheme(Scheme.HTTP);

        // then
        assertTrue(operation.getSchemes().contains(Scheme.HTTP),
                "The newly added scheme must be contained in the schemes list");
    }

    @Test
    public void testScheme() {
        // when
        operation.scheme(Scheme.HTTPS);

        // then
        assertTrue(operation.getSchemes().contains(Scheme.HTTPS),
                "The newly added scheme must be contained in the schemes list");
    }

    @Test
    public void testConsumes() {
        // when
        operation.consumes("consumes");

        // then
        assertTrue(operation.getConsumes().contains("consumes"),
                "The newly added consumes must be contained in the consumes list");
    }

    @Test
    public void testProduces() {
        // when
        operation.produces("produces");

        // then
        assertTrue(operation.getProduces().contains("produces"),
                "The newly added produces must be contained in the produces list");
    }

    @Test
    public void testSecurity() {
        // given
        SecurityRequirement requirement = new SecurityRequirement();
        requirement.setName("name");
        requirement.setScopes(new ArrayList<String>());

        // when
        operation.security(requirement);

        // then
        assertTrue(operation.getSecurity().get(0).keySet().contains("name"),
                "The newly added name must be contained in the security list");

        // given
        requirement.setScopes(null);

        // when
        operation.security(requirement);

        // then
        assertTrue(operation.getSecurity().get(1).get("name").isEmpty(),
                "The security requirement added with a null scope must be empty");
    }

    @Test
    public void testParameter() {
        // given
        Parameter parameter = Mockito.mock(Parameter.class);
        operation.setParameters(null);

        // when
        operation.parameter(parameter);
        assertTrue(operation.getParameters().contains(parameter),
                "The newly added parameter must be contained in the parameters list");
    }

    @Test
    public void testResponse() {
        // given
        Response response = Mockito.mock(Response.class);

        // when
        operation.response(44, response);

        // then
        assertEquals(operation.getResponsesObject().get("44"), response,
                "The newly added response must be contained in the responses map");
    }

    @Test
    public void testDefaultResponse() {
        // given
        Response response = Mockito.mock(Response.class);

        // when
        operation.defaultResponse(response);

        // then
        assertEquals(operation.getResponsesObject().get("default"), response,
                "The default response should be the one that have just been set");
    }

    @Test
    public void testDeprecated() {
        // when
        operation.deprecated(false);

        // then
        assertFalse(operation.isDeprecated(), "Must have deprecated false after set to false");
    }

    @Test
    public void testSetDeprecated() {
        // when
        operation.setDeprecated(true);

        // then
        assertTrue(operation.isDeprecated(), "Must be deprecated after set to true");
    }

    @Test
    public void testVendorExtensions() {
        // given
        String vendorName = "x-vendor";
        String value = "value";

        // when
        operation.setVendorExtension(vendorName, value);
        operation.vendorExtensions(new HashMap<String, Object>());

        // then
        assertEquals(operation.getVendorExtensions().get(vendorName), value,
                "Must be able to retrieve the same value from the map");
    }

    @Test
    public void testTag() {
        // when
        operation.tag("tag");

        // then
        assertTrue(operation.getTags().contains("tag"), "The newly tag  must be contained in the tags list");
    }

    @Test
    public void testSetSummary() {
        // when
        operation.setSummary("summary");

        // then
        assertEquals(operation.getSummary(), "summary", "The get summary must equal the set one");
    }

    @Test
    public void testSetDescription() {
        // when
        operation.setDescription("description");

        // then
        assertEquals(operation.getDescription(), "description", "The get description must equal the set one");
    }

    @Test
    public void testSetOperationId() {
        // when
        operation.setOperationId("operationId");

        // then
        assertEquals(operation.getOperationId(), "operationId", "The get OperaionId must equal the set one");
    }

    @Test
    public void testSetExternalDocs() {
        // given
        ExternalDocs externalDocs = new ExternalDocs();

        // when
        operation.setExternalDocs(externalDocs);

        // then
        assertEquals(operation.getExternalDocs(), externalDocs, "The get externalDocs must equal the set one");
    }

}
