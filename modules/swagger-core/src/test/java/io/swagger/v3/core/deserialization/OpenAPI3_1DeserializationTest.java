package io.swagger.v3.core.deserialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class OpenAPI3_1DeserializationTest {

    @Test
    public void deserializePetstore3_1() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
    }

    @Test
    public void deserializePetstore3_1More() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_more.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
    }


    @Test
    public void deserializePetstore3_0() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore-3.0.yaml");
        final OpenAPI swagger = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), null);
    }

    @Test
    public void deserializeChangelog3_1() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/changelog-3.1.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
        Yaml31.prettyPrint(swagger);
        SerializationMatchers.assertEqualsToYaml31(swagger, jsonString);
    }

    @Test
    public void testDeserializationOnOAS31() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_sample.yaml");
        OpenAPI openAPI = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(openAPI);

        assertEquals(openAPI.getInfo().getTitle(), "Swagger Petstore");
        assertEquals(openAPI.getInfo().getVersion(), "1.0.0");
        assertEquals(openAPI.getInfo().getSummary(), "petstore sample for OAS 3.1.0");
        assertEquals(openAPI.getInfo().getLicense().getName(), "MIT");
        assertEquals(openAPI.getInfo().getLicense().getIdentifier(), "test");

        assertNotNull(openAPI.getWebhooks());
        assertFalse(openAPI.getWebhooks().isEmpty());
        assertNotNull(openAPI.getWebhooks().get("newPet"));
        assertNotNull(openAPI.getWebhooks().get("newPet").getPost());

        assertNotNull(openAPI.getComponents().getPathItems());
        assertNotNull(openAPI.getComponents().getPathItems().get("/pet"));
        assertEquals(openAPI.getComponents().getPathItems().get("/pet").getDescription(), "get a pet");
        assertNotNull(openAPI.getComponents().getPathItems().get("/pet").getGet());
        assertEquals(openAPI.getComponents().getPathItems().get("/pet").getGet().getOperationId(), "getPet");

        assertNotNull(openAPI.getComponents().getSchemas());
        assertNotNull(openAPI.getComponents().getSchemas().get("Pet"));
        assertNotNull(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator());
        assertNotNull(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator().getExtensions());
        assertEquals(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator().getExtensions().get("x-test-extension"), "extended");

        openAPI = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(openAPI);

        assertNull(openAPI.getInfo().getSummary());
        assertNull(openAPI.getWebhooks());
        assertNull(openAPI.getComponents().getPathItems());
        assertNull(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator().getExtensions());

    }

    @Test
    public void testRefDeserializationOnOAS31() throws IOException {
        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_refs_siblings.yaml");
        OpenAPI openAPI = Yaml31.mapper().readValue(jsonString, OpenAPI.class);


        assertEquals(openAPI.getPaths().get("/ref_pet").get$ref(), "#/components/pathItems/pet");
        assertEquals(openAPI.getPaths().get("/ref_pet").getDescription(), "ref pathItem description");
        assertEquals(openAPI.getPaths().get("/ref_pet").getSummary(), "ref pathItem summary");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(0).get$ref(), "#/components/parameters/testParameter");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(0).getDescription(), "ref parameter description");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(0).getSummary(), "ref parameter summary");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getName(), "randomParam");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getIn(), "query");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getExamples().get("refExample").get$ref(), "#/components/examples/testExample");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getExamples().get("refExample").getDescription(), "ref example description");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getExamples().get("refExample").getSummary(), "ref example summary");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getCallbacks().get("callIt").get$ref(), "#/components/callbacks/TestCallback");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getCallbacks().get("callIt").getDescription(), "ref call back description");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getCallbacks().get("callIt").getSummary(), "ref call back summary");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getRequestBody().get$ref(), "#/components/requestBodies/body");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getRequestBody().getDescription(), "ref request body description");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getRequestBody().getSummary(), "ref request body summary");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("201").get$ref(), "#/components/responses/okResponse");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("201").getDescription(), "ref response description");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("201").getSummary(), "ref response summary");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("default").getHeaders().get("head").get$ref(), "#/components/headers/head");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("default").getHeaders().get("head").getDescription(), "ref header description");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("default").getHeaders().get("head").getSummary(), "ref header summary");



    }
}
