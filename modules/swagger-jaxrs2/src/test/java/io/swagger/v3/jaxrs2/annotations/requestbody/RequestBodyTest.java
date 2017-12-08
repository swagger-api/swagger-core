package io.swagger.v3.jaxrs2.annotations.requestbody;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.resources.RequestBodyResource;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Testing RequestBody Resource
 * Created by rafaellopez on 12/6/17.
 */
public class RequestBodyTest {
    @Test(description = "scan class with requesbody annotation")
    public void testRequestBodyAnnotationPriority() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(RequestBodyResource.class);
        Yaml.prettyPrint(openAPI);
        RequestBody getRequestBody = openAPI.getPaths().get("/user").getGet().getRequestBody();
        assertNotNull(getRequestBody);
        assertEquals(getRequestBody.getDescription(), "RequestBody on Annotation");
        RequestBody postRequestBody = openAPI.getPaths().get("/user").getPost().getRequestBody();
        assertNotNull(postRequestBody);
        assertEquals(postRequestBody.getDescription(), "Request Body in Parameter");

    }
}
